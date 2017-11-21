package com.epam.indigoeln.core.service.print.itext2.model.common.image;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides method for converting svg image format
 */
class SvgConverter {
    private SvgConverter() {
    }

    /**
     * Converts svg to png format
     * @param input Input stream of bytes
     * @param output Output stream of bytes
     * @param widthPx Image extension
     */
    static void convertSvg2Png(InputStream input, OutputStream output, float widthPx) {
        TranscoderInput transcoderInput = new TranscoderInput(input);
        TranscoderOutput transcoderOutput = new TranscoderOutput(output);

        PNGTranscoder transcoder = new PNGTranscoder();
        if (widthPx > 0) {
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, widthPx);
        }

        try {
            transcoder.transcode(transcoderInput, transcoderOutput);
            output.flush();
        } catch (TranscoderException | IOException e) {
            throw new SvgConverterException(e);
        }
    }

    public static class SvgConverterException extends RuntimeException {
        SvgConverterException(Throwable cause) {
            super(cause);
        }
    }
}
