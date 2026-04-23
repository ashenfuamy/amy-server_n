package site.ashenstation.app.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.app.config.RabbitMQConfig;
import site.ashenstation.app.dto.CreateVideoDto;
import site.ashenstation.app.dto.UploadChunkDto;
import site.ashenstation.app.dto.VideoTaskDto;
import site.ashenstation.app.dto.VideoTranCodingDto;
import site.ashenstation.app.utils.FFmpegUtils;
import site.ashenstation.app.vo.VideoTaskVo;
import site.ashenstation.entity.*;
import site.ashenstation.enums.ResourceType;
import site.ashenstation.enums.SummaryType;
import site.ashenstation.enums.UploadStatus;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.*;
import site.ashenstation.properties.StaticResourceProperties;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static site.ashenstation.app.config.websocket.WebSocketHandler.ONLINE_SESSIONS;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;
    private final MdaVideoTagMapper videoTagMapper;
    private final SummaryTagMapper summaryTagMapper;

    private final StaticResourceProperties staticResourceProperties;
    private final SummaryParticipantsMapper summaryParticipantsMapper;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;
    private final FFmpegUtils fFmpegUtils;
    private final VideoMapper videoMapper;

    @UpdateResourceCache
    @Transactional
    public VideoTaskVo create(CreateVideoDto dto) {
        Summary summary = summaryService.createSummary(dto);

        summary.setType(SummaryType.VIDEO);

        List<MdaVideoTag> tags = dto.getTags();
        ArrayList<SummaryTag> summaryTags = new ArrayList<>();

        tags.forEach(tag -> {
            if (tag.getId() == null) {
                videoTagMapper.insert(tag);
            }
            SummaryTag summaryTag = new SummaryTag();
            summaryTag.setTagId(tag.getId());
            summaryTag.setSummaryId(summary.getId());
            summaryTags.add(summaryTag);
        });

        summaryTagMapper.insertBatch(summaryTags);

        VideoTaskDto videoTaskDto = new VideoTaskDto();
        videoTaskDto.setSummary(summary);
        videoTaskDto.setTags(tags);

        videoTaskDto.setActors(dto.getActors());
        videoTaskDto.setSize(dto.getSize());
        videoTaskDto.setTotalChunk(dto.getTotalChunk());
        videoTaskDto.setExt(dto.getExt());
        videoTaskDto.setSerialId(null);

        String resourceId = IdUtil.fastSimpleUUID();

        videoTaskDto.setResourceId(resourceId);

        String videoName = IdUtil.fastSimpleUUID() + dto.getExt() + ".temp";
        File videoFolderFile = new File(staticResourceProperties.getVideoRoot(), resourceId);

        videoFolderFile.mkdir();

        File videoNameFile = new File(videoFolderFile, videoName);
        videoTaskDto.setTmpPath(videoNameFile.getAbsolutePath());

        videoTaskDto.setUploadStatus(UploadStatus.UPLOADING);
        redisUtils.set(staticResourceProperties.getUploadCacheKey() + resourceId, videoTaskDto);

        return new VideoTaskVo(resourceId, 0);
    }


    public void uploadChunk(UploadChunkDto dto) {
        VideoTaskDto videoTaskDto = redisUtils.get(staticResourceProperties.getUploadCacheKey() + dto.getId(), VideoTaskDto.class);
        if (videoTaskDto == null) {
            throw new BadRequestException("");
        }

        Integer lastChunk = videoTaskDto.getCurrentChunk();

        Integer currentChunk = lastChunk + 1;

        if (currentChunk.equals(dto.getCurrentChunk())) {
            String tmpPath = videoTaskDto.getTmpPath();

            try {
                FileOutputStream fou = new FileOutputStream(tmpPath, true);
                byte[] buffer = new byte[1024];
                int bytesRead;

                InputStream inputStream = dto.getChunk().getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fou.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                fou.close();

                videoTaskDto.setCurrentChunk(dto.getCurrentChunk());
                redisUtils.set(staticResourceProperties.getUploadCacheKey() + dto.getId(), videoTaskDto);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
    }

    public UploadStatus queryUploadStatus(String id) {
        try {
            VideoTaskDto task = redisUtils.get(staticResourceProperties.getUploadCacheKey() + id, VideoTaskDto.class);
            UploadStatus uploadStatus = task.getUploadStatus();

            if (uploadStatus == UploadStatus.FINISH) {
                redisUtils.del(staticResourceProperties.getUploadCacheKey() + id);
            }

            return uploadStatus;
        } catch (Exception e) {
            return UploadStatus.FINISH;
        }
    }

    public void finishUpload(String id) {
        VideoTaskDto task = redisUtils.get(staticResourceProperties.getUploadCacheKey() + id, VideoTaskDto.class);
        task.setUploadStatus(UploadStatus.TRANSCODING);

        redisUtils.set(staticResourceProperties.getUploadCacheKey() + id, task);
        VideoTranCodingDto videoTranCodingDto = new VideoTranCodingDto(id, SecurityUtils.getCurrentUserId());

        rabbitTemplate.convertAndSend(RabbitMQConfig.UPLOAD_TASK_EXCHANGE, RabbitMQConfig.ROUTING_KEY, JSONObject.toJSONString(videoTranCodingDto));
    }

    @RabbitListener(queues = RabbitMQConfig.UPLOAD_TASK_QUEUE)
    public void receiveProdMessage(String msg, Channel channel, Message message) {

        VideoTranCodingDto videoTranCodingDto = JSONObject.parseObject(msg, VideoTranCodingDto.class);

        try {
            VideoTaskDto task = redisUtils.get(staticResourceProperties.getUploadCacheKey() + videoTranCodingDto.getTaskId(), VideoTaskDto.class);

            String tempAbsolutePath = task.getTmpPath();
            String videoPath = tempAbsolutePath.replace(".temp", "");
            File videoFile = new File(videoPath);

            new File(tempAbsolutePath).renameTo(videoFile);

            Long duration = fFmpegUtils.getDuration(videoPath);

            ProgressListener progressListener = new ProgressListener() {
                final double duration_ns = duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    double percentage = progress.out_time_ns / duration_ns;

                    String jsonString = JSONObject.toJSONString(new HashMap<>() {{
                        put("event", "upload.transcoding");
                        put("args", videoTranCodingDto.getTaskId() + "," + String.valueOf(percentage * 100));
                    }});

                    TextMessage textMessage = new TextMessage(jsonString);

                    ONLINE_SESSIONS.forEach((s, webSocketSession) -> {
                        if (s.startsWith(videoTranCodingDto.getUserId())) {
                            try {
                                webSocketSession.sendMessage(textMessage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            };


            File transFileTemp = videoFile;
            if (!FileNameUtil.extName(videoFile.getAbsoluteFile()).equals("mp4")) {
                transFileTemp = fFmpegUtils.conversionToMP4(videoPath, progressListener);
            }

            transFileTemp = fFmpegUtils.conversionToTs(transFileTemp.getAbsolutePath(), progressListener);
            File m38u = fFmpegUtils.conversionToM38u(transFileTemp.getAbsolutePath(), progressListener);

            Summary summary = task.getSummary();

            summaryMapper.insert(summary);

            Video mdaVideo = new Video();
            mdaVideo.setSize(task.getSize());
//            mdaVideo.setConverted(true);
            mdaVideo.setSeriesId(null);
            mdaVideo.setFilePath(m38u.getAbsolutePath());
            mdaVideo.setFolderPath(m38u.getParentFile().getAbsolutePath());
            mdaVideo.setFileUrl(staticResourceProperties.getVideoResourcePrefix() + "/" + task.getResourceId() + "/" + m38u.getName());
            mdaVideo.setSummaryId(summary.getId());
            mdaVideo.setDuration(duration);

            videoMapper.insert(mdaVideo);

            List<MdaVideoTag> tags = task.getTags();
            ArrayList<SummaryTag> summaryTags = new ArrayList<>();

            tags.forEach(tag -> {
                if (tag.getId() == null) {
                    videoTagMapper.insert(tag);
                }
                SummaryTag summaryTag = new SummaryTag();
                summaryTag.setTagId(tag.getId());
                summaryTag.setSummaryId(summary.getId());
                summaryTags.add(summaryTag);
            });

            summaryTagMapper.insertBatch(summaryTags);


            ArrayList<SummaryParticipants> summaryParticipants = new ArrayList<>();
            List<String> actors = task.getActors();
            actors.forEach(actor -> {
                SummaryParticipants participants = new SummaryParticipants();
                participants.setSummaryId(summary.getId());
                participants.setActorId(actor);
                summaryParticipants.add(participants);
            });

            summaryParticipantsMapper.insertBatch(summaryParticipants);
            appUserResourcePermissionMapper.insert(new AppUserResourcePermission(task.getResourceId(), videoTranCodingDto.getUserId(), ResourceType.VIDEO));

            task.setUploadStatus(UploadStatus.FINISH);


            ONLINE_SESSIONS.forEach((s, webSocketSession) -> {
                if (s.startsWith(videoTranCodingDto.getUserId())) {
                    try {
                        webSocketSession.sendMessage(new TextMessage(JSONObject.toJSONString(new HashMap<>() {{
                            put("event", "upload.finish");
                            put("args", videoTranCodingDto.getTaskId());
                        }})));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


            redisUtils.del(staticResourceProperties.getUploadCacheKey() + videoTranCodingDto.getTaskId());
        } catch (RuntimeException | IOException e) {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            throw new RuntimeException(e);

        }


        System.out.println();

    }
}
