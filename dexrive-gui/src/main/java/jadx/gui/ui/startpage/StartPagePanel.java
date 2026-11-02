package jadx.gui.ui.startpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import jadx.gui.settings.JadxSettings;
import jadx.gui.ui.MainWindow;
import jadx.gui.ui.dialog.DexriveInspector;
import jadx.gui.ui.panel.ContentPanel;
import jadx.gui.ui.tab.TabbedPane;
import jadx.gui.utils.Icons;
import jadx.gui.utils.UiUtils;

/**
 * First-run and workspace dashboard. Fluid 3-column/1-column actions, scalable type,
 * keyboard accessible Swing buttons and persistent recent projects.
 */
public class StartPagePanel extends ContentPanel {
    private static final long serialVersionUID = 2457805175218770732L;
    private static final Color AQUA = new Color(0x34D9D0);
    private static final Color MUTED = new Color(0xA7B7C9);

    private final RecentProjectsJList recentList;
    private final DefaultListModel<RecentProjectItem> recentListModel;
    private final MainWindow mainWindow;
    private final JadxSettings settings;
    private final JPanel actionGrid;

    public static int hoveredRemoveBtnIndex = -1;

    public StartPagePanel(TabbedPane tabbedPane, StartPageNode node) {
        super(tabbedPane, node);
        this.mainWindow = tabbedPane.getMainWindow();
        this.settings = mainWindow.getSettings();
        Font font = settings.getUiFont();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel layout = new JPanel(new BorderLayout(0, 24));
        layout.setOpaque(false);
        layout.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));

        JPanel top = new JPanel(new BorderLayout(16, 12));
        top.setOpaque(false);
        JLabel logo = new JLabel(new ImageIcon(UiUtils.openImage("/logos/dexrive-logo-48px.png")));
        logo.getAccessibleContext().setAccessibleDescription("DEXRIVE brand mark");
        top.add(logo, BorderLayout.WEST);
        JPanel headings = new JPanel();
        headings.setOpaque(false);
        headings.setLayout(new BoxLayout(headings, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel("DEXRIVE");
        title.setForeground(AQUA);
        title.setFont(font.deriveFont(Font.BOLD, Math.max(28f, font.getSize2D() * 2.25f)));
        title.setAlignmentX(LEFT_ALIGNMENT);
        headings.add(title);
        headings.add(Box.createVerticalStrut(5));
        JLabel caption = new JLabel("Explore beyond the source.  •  by Brendigo");
        caption.setFont(font.deriveFont(Math.max(13f, font.getSize2D())));
        caption.setForeground(MUTED);
        caption.setAlignmentX(LEFT_ALIGNMENT);
        headings.add(caption);
        top.add(headings, BorderLayout.CENTER);

        actionGrid = new JPanel(new GridLayout(1, 3, 12, 12));
        actionGrid.setOpaque(false);
        JButton openPackage = action("Open package", "APK / AAB / DEX", Icons.OPEN, font);
        openPackage.addActionListener(e -> mainWindow.openFileDialog());
        JButton openWorkspace = action("Open project", "Continue your analysis", Icons.OPEN_PROJECT, font);
        openWorkspace.addActionListener(e -> mainWindow.openProjectDialog());
        JButton inspect = action("APK Inspector", "Local package overview", Icons.DEXRIVE_INSPECTOR, font);
        inspect.addActionListener(e -> DexriveInspector.open(mainWindow));
        actionGrid.add(openPackage);
        actionGrid.add(openWorkspace);
        actionGrid.add(inspect);

        JPanel welcome = new JPanel(new BorderLayout(0, 20));
        welcome.setOpaque(false);
        welcome.add(top, BorderLayout.NORTH);
        welcome.add(actionGrid, BorderLayout.CENTER);
        layout.add(welcome, BorderLayout.NORTH);

        recentListModel = new DefaultListModel<>();
        recentList = new RecentProjectsJList(recentListModel);
        recentList.setCellRenderer(new RecentProjectListCellRenderer(font));
        recentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.setFixedCellHeight(Math.max(54, font.getSize() * 4));
        recentList.getAccessibleContext().setAccessibleName("Recent projects");
        recentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = recentList.locationToIndex(e.getPoint());
                if (index == -1 || index >= recentListModel.size()) {
                    return;
                }
                Rectangle bounds = recentList.getCellBounds(index, index);
                if (bounds == null || !bounds.contains(e.getPoint())) {
                    return;
                }
                recentList.setSelectedIndex(index);
                RecentProjectItem item = recentListModel.getElementAt(index);
                if (SwingUtilities.isRightMouseButton(e)) {
                    showRecentProjectContextMenu(e);
                    return;
                }
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                RecentProjectListCellRenderer renderer = (RecentProjectListCellRenderer) recentList.getCellRenderer()
                        .getListCellRendererComponent(recentList, item, index, true, false);
                renderer.setSize(bounds.getSize());
                renderer.doLayout();
                // Renderer computes the clickable close-button bounds when painted.
                java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
                        Math.max(1, bounds.width), Math.max(1, bounds.height),
                        java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D graphics = image.createGraphics();
                try {
                    renderer.paint(graphics);
                } finally {
                    graphics.dispose();
                }
                Rectangle remove = renderer.getRemoveIconBounds();
                if (remove != null && remove.contains(e.getX() - bounds.x, e.getY() - bounds.y)) {
                    removeRecentProject(item.getPath());
                } else if (e.getClickCount() == 2) {
                    mainWindow.open(item.getPath());
                }
            }
        });

        JPanel recentCard = new JPanel(new BorderLayout(0, 13));
        recentCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        recentCard.putClientProperty("FlatLaf.style", "arc: 18");
        JLabel recentTitle = new JLabel("RECENT WORKSPACES");
        recentTitle.setFont(font.deriveFont(Font.BOLD, Math.max(13f, font.getSize2D())));
        recentCard.add(recentTitle, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(recentList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(550, 250));
        recentCard.add(scrollPane, BorderLayout.CENTER);

        layout.add(recentCard, BorderLayout.CENTER);
        JLabel privacy = new JLabel("LOCAL WORKSPACE  •  by Brendigo  •  brendigo.com");
        privacy.setForeground(MUTED);
        privacy.setFont(font.deriveFont(Font.PLAIN, Math.max(11f, font.getSize2D() - 1)));
        layout.add(privacy, BorderLayout.SOUTH);

        JPanel centered = new JPanel(new BorderLayout());
        centered.setOpaque(false);
        centered.add(layout, BorderLayout.NORTH);
        JScrollPane pageScroll = new JScrollPane(centered);
        pageScroll.setBorder(BorderFactory.createEmptyBorder());
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(pageScroll, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            private boolean narrow;
            @Override
            public void componentResized(ComponentEvent event) {
                boolean useNarrow = getWidth() < 680;
                if (useNarrow != narrow) {
                    narrow = useNarrow;
                    actionGrid.setLayout(new GridLayout(narrow ? 3 : 1, narrow ? 1 : 3, 12, 12));
                    actionGrid.revalidate();
                }
            }
        });
        fillRecentProjectsList();
    }

    private static JButton action(String label, String detail, javax.swing.Icon icon, Font font) {
        JButton button = new JButton("<html><b>" + label + "</b><br><small>" + detail + "</small></html>", icon);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setIconTextGap(14);
        button.setFont(font.deriveFont(Font.BOLD, Math.max(13f, font.getSize2D())));
        button.setPreferredSize(new Dimension(180, 82));
        button.putClientProperty("FlatLaf.style", "arc: 16; margin: 14,14,14,14");
        button.getAccessibleContext().setAccessibleDescription(label + " - " + detail);
        return button;
    }

    private void fillRecentProjectsList() {
        recentListModel.clear();
        List<Path> paths = settings.getRecentProjects();
        for (Path path : paths) {
            recentListModel.addElement(new RecentProjectItem(path));
        }
    }

    private void removeRecentProject(Path path) {
        settings.removeRecentProject(path);
        fillRecentProjectsList();
    }

    private void showRecentProjectContextMenu(MouseEvent event) {
        RecentProjectItem selected = recentList.getSelectedValue();
        if (selected == null) {
            return;
        }
        JPopupMenu menu = new JPopupMenu();
        JMenuItem open = new JMenuItem("Open project");
        open.addActionListener(e -> mainWindow.open(selected.getPath()));
        menu.add(open);
        JMenuItem remove = new JMenuItem("Remove from recent");
        remove.addActionListener(e -> removeRecentProject(selected.getPath()));
        menu.add(remove);
        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    @Override
    public void loadSettings() {
        // The parent workspace manages theme and font reloads.
    }

    private static final class RecentProjectsJList extends JList<RecentProjectItem> {
        private static final long serialVersionUID = 1L;

        RecentProjectsJList(DefaultListModel<RecentProjectItem> model) {
            super(model);
        }

        @Override
        public String getToolTipText(MouseEvent e) {
            int index = locationToIndex(e.getPoint());
            if (index < 0 || index >= getModel().getSize()) {
                return null;
            }
            Rectangle b = getCellBounds(index, index);
            if (b == null || !b.contains(e.getPoint())) {
                return null;
            }
            return getModel().getElementAt(index).getAbsolutePath();
        }
    }
}
