/*
 * This file is part of SourceHeader project.
 * licence: FreeBSD licence.
 *
 * @category Utils
 *
 * file: ColorsMap.java
 */

package sourceheader.gui.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import sourceheader.core.FileHeader;

/**
 * Represents map of colors to headers.
 * This is singleton class.
 *
 * It handles distribution to new headers and provides information
 * about color and also about color's icon.
 *
 * @author steve
 */
public class ColorsMap {

    private ColorsMap() {
    }

    private static ColorsMap instance = new ColorsMap();
    private static final int ICON_SIZE = 10;

    public static ColorsMap getInstance() {
        return instance;
    }

    private Dictionary<FileHeader, HeaderInfo> headersMap =
            new Hashtable<FileHeader, HeaderInfo>();

    private final List<HeaderInfo> colorsList =
            new ArrayList<HeaderInfo>() {
                {
                    add(new HeaderInfo(Color.RED));
                    add(new HeaderInfo(Color.BLUE));
                    add(new HeaderInfo(Color.YELLOW));
                    add(new HeaderInfo(Color.GREEN));
                }};

    private final HeaderInfo lastInfo =
            new HeaderInfo(Color.ORANGE, createUniversalIcon());

    /**
     * @param header
     * @return Returns color that is assigned to given header.
     */
    public Color getColorForHeader(FileHeader header) {
        return this.getInfoFor(header).color;
    }

    public Icon getIconForHeader(FileHeader header) {
        return this.getInfoFor(header).icon;
    }

    private HeaderInfo getInfoFor(FileHeader header) {
        HeaderInfo info = this.headersMap.get(header);
        if (info == null) {
            if (this.colorsList.size() == 0) {
                return this.lastInfo;
            }

            info = this.colorsList.get(0);
            this.colorsList.remove(0);
            this.headersMap.put(header, info);
        }

        return info;
    }

    private Icon createIcon(Color color) {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, Transparency.BITMASK);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
        return new ImageIcon(image);
    }

    private static Icon createUniversalIcon() {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, Transparency.BITMASK);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.black);
        graphics.fillArc(0, 0, ICON_SIZE, ICON_SIZE, 0, 180);
        graphics.fillOval(ICON_SIZE/3, (3*ICON_SIZE)/5, ICON_SIZE/4, ICON_SIZE/4);
        graphics.drawOval(0, 0, ICON_SIZE-1, ICON_SIZE-1);
        graphics.setColor(Color.white);
        graphics.fillOval(ICON_SIZE/3, ICON_SIZE/7, ICON_SIZE/4, ICON_SIZE/4);
        return new ImageIcon(image);
    }

    private class HeaderInfo {
        public HeaderInfo(Color color) {
            this.color = color;
            this.icon = createIcon(color);
        }

        public HeaderInfo(Color color, Icon icon) {
            this.icon = icon;
            this.color = color;
        }

        public Icon icon;
        public Color color;
    }
}
