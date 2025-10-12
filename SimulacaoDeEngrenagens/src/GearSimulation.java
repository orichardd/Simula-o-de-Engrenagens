import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class GearSimulation {
    public static void main(String[] args) {
        new ConfigScreen();
    }
}

// === FASE 1: TELA DE CONFIGURAÇÃO ===
class ConfigScreen extends JFrame implements ActionListener {
    JTextField txtTeeth1, txtTeeth2, txtRpm, txtTorque;
    JButton btnStart;

    ConfigScreen() {
        setTitle("Configuração das Engrenagens");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Dentes engrenagem fonte:"));
        txtTeeth1 = new JTextField("20"); add(txtTeeth1);

        add(new JLabel("Dentes engrenagem 2:"));
        txtTeeth2 = new JTextField("40"); add(txtTeeth2);

        add(new JLabel("RPM da fonte:"));
        txtRpm = new JTextField("100"); add(txtRpm);

        add(new JLabel("Torque da fonte:"));
        txtTorque = new JTextField("10"); add(txtTorque);

        btnStart = new JButton("Iniciar Simulação");
        btnStart.addActionListener(this);
        add(btnStart);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        int teeth1 = Integer.parseInt(txtTeeth1.getText());
        int teeth2 = Integer.parseInt(txtTeeth2.getText());
        double rpm = Double.parseDouble(txtRpm.getText());
        double torque = Double.parseDouble(txtTorque.getText());
        dispose();
        new MainScreen(teeth1, teeth2, rpm, torque);
    }
}

// === FASE 2: VISUALIZAÇÃO PRINCIPAL ===
class MainScreen extends JFrame {
    int teeth1, teeth2;
    double rpm1, rpm2;
    double torque1, torque2;

    JSlider speedSlider, torqueSlider;
    ArrayList<Double> rpmHistory = new ArrayList<>();
    ArrayList<Double> torqueHistory = new ArrayList<>();

    GearCanvas canvas;

    MainScreen(int t1, int t2, double rpm, double torque) {
        this.teeth1 = t1;
        this.teeth2 = t2;
        this.rpm1 = rpm;
        this.torque1 = torque;
        this.rpm2 = rpm * ((double)t1 / t2);
        this.torque2 = torque * ((double)t2 / t1);

        setTitle("Simulação de Engrenagens");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel esquerdo
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 720));
        leftPanel.setLayout(new GridLayout(8, 1));
        JLabel lblRpm1 = new JLabel();
        JLabel lblRpm2 = new JLabel();
        JLabel lblTorque1 = new JLabel();
        JLabel lblTorque2 = new JLabel();

        speedSlider = new JSlider(0, 200, (int)rpm1);
        torqueSlider = new JSlider(0, 50, (int)torque1);
        leftPanel.add(new JLabel("RPM Fonte"));
        leftPanel.add(speedSlider);
        leftPanel.add(new JLabel("Torque Fonte"));
        leftPanel.add(torqueSlider);
        leftPanel.add(lblRpm1);
        leftPanel.add(lblRpm2);
        leftPanel.add(lblTorque1);
        leftPanel.add(lblTorque2);
        add(leftPanel, BorderLayout.WEST);

        // Canvas central
        canvas = new GearCanvas(teeth1, teeth2);
        add(canvas, BorderLayout.CENTER);

        // === BOTÃO DE REINICIAR SIMULAÇÃO ===
        JButton btnReset = new JButton("Nova Simulação");
        btnReset.addActionListener(e -> {
            dispose();
            new ConfigScreen();
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(btnReset);
        add(topPanel, BorderLayout.NORTH);

        // Atualização contínua
        Timer timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rpm1 = speedSlider.getValue();
                torque1 = torqueSlider.getValue();
                rpm2 = rpm1 * ((double)teeth1 / teeth2);
                torque2 = torque1 * ((double)teeth2 / teeth1);
                lblRpm1.setText("RPM Fonte: " + (int)rpm1);
                lblRpm2.setText("RPM 2: " + (int)rpm2);
                lblTorque1.setText("Torque Fonte: " + String.format("%.2f", torque1));
                lblTorque2.setText("Torque 2: " + String.format("%.2f", torque2));
                rpmHistory.add(rpm1);
                torqueHistory.add(torque1);
                if (rpmHistory.size() > 200) {
                    rpmHistory.remove(0);
                    torqueHistory.remove(0);
                }
                canvas.updateRotation(rpm1, rpm2);
                canvas.setGraphData(rpmHistory, torqueHistory);
                canvas.repaint();
            }
        });
        timer.start();

        setVisible(true);
    }
}

// === CANVAS DE DESENHO ===
class GearCanvas extends JPanel {
    int teeth1, teeth2;
    double angle1 = 0, angle2 = 0;
    ArrayList<Double> rpmHist, torqueHist;

    int radius1, radius2, cx1, cx2, cy;

    GearCanvas(int t1, int t2) {
        this.teeth1 = t1;
        this.teeth2 = t2;
        setBackground(Color.BLACK);

        // Raio proporcional aos dentes
        radius1 = 5 * t1;
        radius2 = 5 * t2;

        // Calcular posições centradas e engrenadas (com folga para encaixe)
        cy = 360;
        int distance = radius1 + radius2 - 8; // -8 para deixar os dentes "entremeados"
        cx1 = 640 - distance / 2;
        cx2 = cx1 + distance;
    }

    void updateRotation(double rpm1, double rpm2) {
        angle1 += rpm1 / 500;
        // Adiciona meia fase para sincronizar dentes
        angle2 = -angle1 * ((double)teeth1 / teeth2) + Math.PI / teeth2;
    }

    void setGraphData(ArrayList<Double> rpm, ArrayList<Double> torque) {
        this.rpmHist = rpm;
        this.torqueHist = torque;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenhar engrenagens engrenadas
        drawGear(g2, cx1, cy, radius1, teeth1, angle1, new Color(180, 180, 180));
        drawGear(g2, cx2, cy, radius2, teeth2, angle2, new Color(120, 120, 120));

        // Desenhar gráfico simples
        int gx = getWidth() - 400, gy = getHeight() - 200;
        g2.setColor(Color.WHITE);
        g2.drawRect(gx, gy, 350, 150);
        g2.drawString("Verde = RPM | Vermelho = Torque", gx, gy - 5);

        if (rpmHist != null) {
            for (int i = 1; i < rpmHist.size(); i++) {
                int x1 = gx + i;
                int x2 = gx + i + 1;
                int y1 = gy + 150 - (int)(rpmHist.get(i-1) / 2);
                int y2 = gy + 150 - (int)(rpmHist.get(i) / 2);
                g2.setColor(Color.GREEN);
                g2.drawLine(x1, y1, x2, y2);
                int t1 = gy + 150 - (int)(torqueHist.get(i-1) * 3);
                int t2 = gy + 150 - (int)(torqueHist.get(i) * 3);
                g2.setColor(Color.RED);
                g2.drawLine(x1, t1, x2, t2);
            }
        }
    }

    void drawGear(Graphics2D g2, int cx, int cy, int radius, int teeth, double angle, Color color) {
        g2.setColor(color);
        double toothAngle = 2 * Math.PI / teeth;
        Polygon gear = new Polygon();

        for (int i = 0; i < teeth; i++) {
            double a1 = angle + i * toothAngle;
            double a2 = a1 + toothAngle / 2;
            double a3 = a1 + toothAngle;

            int innerX1 = cx + (int)(Math.cos(a1) * (radius - 8));
            int innerY1 = cy + (int)(Math.sin(a1) * (radius - 8));

            int outerX = cx + (int)(Math.cos(a2) * (radius + 8));
            int outerY = cy + (int)(Math.sin(a2) * (radius + 8));

            int innerX2 = cx + (int)(Math.cos(a3) * (radius - 8));
            int innerY2 = cy + (int)(Math.sin(a3) * (radius - 8));

            gear.addPoint(innerX1, innerY1);
            gear.addPoint(outerX, outerY);
            gear.addPoint(innerX2, innerY2);
        }

        g2.fillPolygon(gear);

        // Buraco central (eixo)
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(cx - radius / 5, cy - radius / 5, radius / 2, radius / 2);

        // Texto no centro
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String text = teeth + " dentes";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx - fm.stringWidth(text) / 2, cy + fm.getAscent() / 2);
    }
}
