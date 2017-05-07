// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.image;

import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * A plugin SPI for the ImageIO utility.  This reads in DDS-formatted images
 * (Experimental)
 * 
 * @author AFRL/RQQD
 */
public class DDSImageReaderSpi extends ImageReaderSpi {

    public DDSImageReaderSpi() {
        super("none",
                "1.0",
                new String[]{"DDS"},
                new String[]{"dds"},
                new String[]{"image/jpeg"},
                "avtas.map.image.DDSImageReader",
                new Class[]{ImageInputStream.class},
                new String[]{"avtas.map.image.DDSImageReaderSpi"},
                false,
                "",
                "",
                null,
                null,
                false,
                "",
                "",
                null,
                null);
    }

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        if (source instanceof ImageInputStream) {
            ImageInputStream iis = (ImageInputStream) source;
            iis.mark();
            byte[] magic = new byte[4];
            iis.readFully(magic);
            iis.reset();
            
            return new String(magic).equals("DDS ");
        }
        return false;
    }

    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new DDSReader(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "Experimental DDS Image Reader";
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */