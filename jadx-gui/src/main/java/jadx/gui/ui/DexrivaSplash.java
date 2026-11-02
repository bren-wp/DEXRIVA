package jadx.gui.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JWindow;
import javax.swing.JPanel;

import jadx.gui.utils.UiUtils;

/**
 * DEXRIVE startup identity. Uses original Java2D shapes, no third-party brand assets.
 * Shown only while the main desktop window is being initialized; no artificial delay.
 */
public final class DexrivaSplash extends JWindow {
    private static final long serialVersionUID = 1L;
    private static final Color NAVY = new Color(0x101927);
    private static final Color AQUA = new Color(0x34D9D0);
    private static final Color MUTED = new Color(0xA7B7C9);

    public DexrivaSplash() {
        setContentPane(new SplashCanvas());
        UiUtils.setWindowIcons(this);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(560, Math.max(320, screen.width - 40)),
                Math.min(312, Math.max(230, screen.height - 60)));
        setLocationRelativeTo(null);
        getAccessibleContext().setAccessibleName("DEXRIVE starting");
    }

    private static final class SplashCanvas extends JPanel {
        private static final long serialVersionUID = 1L;

        SplashCanvas() {
            setOpaque(true);
            setBackground(NAVY);
            setPreferredSize(new Dimension(560, 312));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                float k = Math.min(w / 560f, h / 312f);
                g.translate((w - 560 * k) / 2f, (h - 312 * k) / 2f);
                g.scale(k, k);

                g.setPaint(new java.awt.GradientPaint(0, 0, new Color(0x172A3F), 560, 312, NAVY));
                g.fillRoundRect(0, 0, 560, 312, 24, 24);
                g.setColor(new Color(0x2D475B));
                g.setStroke(new BasicStroke(1));
                g.draw(new RoundRectangle2D.Float(1, 1, 558, 310, 22, 22));

                g.setColor(new Color(0x1C3B4B));
                g.fillRoundRect(45, 76, 98, 98, 27, 27);
                g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(AQUA);
                g.drawPolyline(new int[] {73, 94, 73}, new int[] {103, 125, 147}, 3);
                g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(Color.WHITE);
                g.drawLine(100, 148, 119, 148);

                g.setColor(new Color(0xF3F7FB));
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
                g.drawString("DEXRIVE", 168, 115);
                g.setColor(AQUA);
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
                g.drawString("ANDROID CODE EXPLORER", 171, 145);

                g.setColor(MUTED);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                g.drawString("Explore beyond the source.", 47, 221);

                g.setColor(new Color(0x2A4054));
                g.fillRoundRect(47, 251, 465, 5, 5, 5);
                g.setColor(AQUA);
                g.fillRoundRect(47, 251, 145, 5, 5, 5);
                g.setColor(MUTED);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
                g.drawString("PREPARING WORKSPACE", 47, 281);
                g.drawString("Brendigo", 478, 281);
            } finally {
                g.dispose();
            }
        }
    }
}
