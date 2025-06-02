package logic.video_processing.audio_extractor;

import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;

public class AudioValidator {

    public static boolean validate (FFmpegFrameGrabber grabber) throws FFmpegFrameGrabber.Exception {
        AVFormatContext fmtCtx = grabber.getFormatContext();
        for (int i = 0; i < fmtCtx.nb_streams(); i++) {
            AVStream stream = fmtCtx.streams(i);
            if (stream.codecpar().codec_type() == avutil.AVMEDIA_TYPE_AUDIO) {
                return true;
            }
        }
        grabber.stop();
        return false;
    }
}