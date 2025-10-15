import java.awt.*; // Importa classes gráficas básicas (cores, fontes, etc)
import java.awt.event.*; // Importa classes para lidar com eventos (como cliques)
import javax.swing.*; // Importa os componentes da interface gráfica Swing
import java.util.ArrayList; // Importa a classe ArrayList para listas dinâmicas

public class GearSimulation {
    public static void main(String[] args) { // Método principal
        new ConfigScreen(); // Cria a tela de configuração ao iniciar o programa
    }
}

// === FASE 1: TELA DE CONFIGURAÇÃO ===
class ConfigScreen extends JFrame implements ActionListener { // Classe da tela inicial, herda JFrame e implementa ActionListener
    JTextField txtTeeth1, txtTeeth2, txtRpm, txtTorque; // Campos de texto para entrada do usuário
    JButton btnStart; // Botão para iniciar a simulação

    ConfigScreen() { // Construtor da classe
        setTitle("Configuração das Engrenagens"); // Define o título da janela
        setSize(400, 300); // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        setLayout(new GridLayout(5, 2, 5, 5)); // Define o layout em grade (5 linhas, 2 colunas, espaçamento 5px)

        add(new JLabel("Dentes engrenagem fonte:")); // Adiciona o texto do primeiro rótulo
        txtTeeth1 = new JTextField("20"); add(txtTeeth1); // Cria e adiciona campo de texto com valor inicial "20"

        add(new JLabel("Dentes engrenagem 2:")); // Rótulo para a segunda engrenagem
        txtTeeth2 = new JTextField("40"); add(txtTeeth2); // Campo de texto com valor inicial "40"

        add(new JLabel("RPM da fonte:")); // Rótulo do RPM
        txtRpm = new JTextField("100"); add(txtRpm); // Campo de texto com valor inicial "100"

        add(new JLabel("Torque da fonte:")); // Rótulo do torque
        txtTorque = new JTextField("10"); add(txtTorque); // Campo de texto com valor inicial "10"

        btnStart = new JButton("Iniciar Simulação"); // Cria o botão
        btnStart.addActionListener(this); // Define que a própria classe reagirá ao clique
        add(btnStart); // Adiciona o botão ao layout

        setVisible(true); // Exibe a janela
    }

    public void actionPerformed(ActionEvent e) { // Método chamado quando o botão é clicado
        int teeth1 = Integer.parseInt(txtTeeth1.getText()); // Lê e converte para inteiro os dentes da engrenagem 1
        int teeth2 = Integer.parseInt(txtTeeth2.getText()); // Lê e converte para inteiro os dentes da engrenagem 2
        double rpm = Double.parseDouble(txtRpm.getText()); // Lê e converte o RPM
        double torque = Double.parseDouble(txtTorque.getText()); // Lê e converte o torque
        dispose(); // Fecha a janela atual
        new MainScreen(teeth1, teeth2, rpm, torque); // Cria a tela principal da simulação
    }
}

// === FASE 2: VISUALIZAÇÃO PRINCIPAL ===
class MainScreen extends JFrame { // Classe da tela principal
    int teeth1, teeth2; // Dentes das engrenagens
    double rpm1, rpm2; // RPM das engrenagens
    double torque1, torque2; // Torque das engrenagens

    JSlider speedSlider, torqueSlider; // Sliders para ajustar RPM e torque
    ArrayList<Double> rpmHistory = new ArrayList<>(); // Histórico de RPM
    ArrayList<Double> torqueHistory = new ArrayList<>(); // Histórico de torque

    GearCanvas canvas; // Painel onde as engrenagens serão desenhadas

    MainScreen(int t1, int t2, double rpm, double torque) { // Construtor que recebe as configurações iniciais
        this.teeth1 = t1; // Armazena dentes da engrenagem 1
        this.teeth2 = t2; // Armazena dentes da engrenagem 2
        this.rpm1 = rpm; // Armazena RPM inicial da engrenagem 1
        this.torque1 = torque; // Armazena torque inicial da engrenagem 1
        this.rpm2 = rpm * ((double)t1 / t2); // Calcula RPM da engrenagem 2 (inversamente proporcional ao número de dentes)
        this.torque2 = torque * ((double)t2 / t1); // Calcula torque da engrenagem 2 (proporcional aos dentes)

        setTitle("Simulação de Engrenagens"); // Define título da janela
        setSize(1920, 1080); // Define tamanho da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        setLayout(new BorderLayout()); // Usa layout com regiões (N, S, E, W, Center)

        // Painel esquerdo
        JPanel leftPanel = new JPanel(); // Cria painel lateral
        leftPanel.setPreferredSize(new Dimension(200, 720)); // Define tamanho preferido
        leftPanel.setLayout(new GridLayout(8, 1)); // Layout em grade de 8 linhas

        JLabel lblRpm1 = new JLabel(); // Texto dinâmico do RPM 1
        JLabel lblRpm2 = new JLabel(); // Texto dinâmico do RPM 2
        JLabel lblTorque1 = new JLabel(); // Texto dinâmico do torque 1
        JLabel lblTorque2 = new JLabel(); // Texto dinâmico do torque 2

        speedSlider = new JSlider(0, 200, (int)rpm1); // Slider de RPM (mínimo 0, máximo 200)
        torqueSlider = new JSlider(0, 50, (int)torque1); // Slider de torque (mínimo 0, máximo 50)

        leftPanel.add(new JLabel("RPM Fonte")); // Texto fixo
        leftPanel.add(speedSlider); // Adiciona o controle deslizante de RPM
        leftPanel.add(new JLabel("Torque Fonte")); // Texto fixo
        leftPanel.add(torqueSlider); // Adiciona o controle de torque
        leftPanel.add(lblRpm1); // Exibe RPM atual da engrenagem 1
        leftPanel.add(lblRpm2); // Exibe RPM atual da engrenagem 2
        leftPanel.add(lblTorque1); // Exibe torque atual da engrenagem 1
        leftPanel.add(lblTorque2); // Exibe torque atual da engrenagem 2
        add(leftPanel, BorderLayout.WEST); // Adiciona o painel à esquerda

        // Canvas central
        canvas = new GearCanvas(teeth1, teeth2); // Cria o painel que desenha as engrenagens
        add(canvas, BorderLayout.CENTER); // Adiciona ao centro da janela

        // === BOTÃO DE REINICIAR SIMULAÇÃO ===
        JButton btnReset = new JButton("Nova Simulação"); // Botão de reset
        btnReset.addActionListener(e -> { // Ação quando clicado
            dispose(); // Fecha janela atual
            new ConfigScreen(); // Volta à tela inicial
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Painel superior alinhado à direita
        topPanel.add(btnReset); // Adiciona o botão
        add(topPanel, BorderLayout.NORTH); // Coloca no topo

        // Atualização contínua
        Timer timer = new Timer(30, new ActionListener() { // Timer dispara a cada 30ms
            public void actionPerformed(ActionEvent e) { // Executa a cada frame
                rpm1 = speedSlider.getValue(); // Atualiza RPM conforme slider
                torque1 = torqueSlider.getValue(); // Atualiza torque conforme slider
                rpm2 = rpm1 * ((double)teeth1 / teeth2); // Recalcula RPM da engrenagem 2
                torque2 = torque1 * ((double)teeth2 / teeth1); // Recalcula torque da engrenagem 2
                lblRpm1.setText("RPM Fonte: " + (int)rpm1); // Mostra RPM 1
                lblRpm2.setText("RPM 2: " + (int)rpm2); // Mostra RPM 2
                lblTorque1.setText("Torque Fonte: " + String.format("%.2f", torque1)); // Mostra torque 1
                lblTorque2.setText("Torque 2: " + String.format("%.2f", torque2)); // Mostra torque 2
                rpmHistory.add(rpm1); // Guarda o histórico de RPM
                torqueHistory.add(torque1); // Guarda o histórico de torque
                if (rpmHistory.size() > 200) { // Limita tamanho do histórico
                    rpmHistory.remove(0); // Remove primeiro valor (antigo)
                    torqueHistory.remove(0); // Remove também torque correspondente
                }
                canvas.updateRotation(rpm1, rpm2); // Atualiza ângulos das engrenagens
                canvas.setGraphData(rpmHistory, torqueHistory); // Envia dados do gráfico
                canvas.repaint(); // Redesenha o canvas
            }
        });
        timer.start(); // Inicia o loop de atualização

        setVisible(true); // Exibe a janela
    }
}

// === CANVAS DE DESENHO ===
class GearCanvas extends JPanel { // Painel personalizado para desenhar as engrenagens
    int teeth1, teeth2; // Número de dentes das engrenagens
    double angle1 = 0, angle2 = 0; // Ângulos de rotação
    ArrayList<Double> rpmHist, torqueHist; // Dados para o gráfico

    int radius1, radius2, cx1, cx2, cy; // Raio e coordenadas de posição

    GearCanvas(int t1, int t2) { // Construtor
        this.teeth1 = t1; // Armazena dentes da engrenagem 1
        this.teeth2 = t2; // Armazena dentes da engrenagem 2
        setBackground(Color.BLACK); // Fundo preto

        // Raio proporcional ao número de dentes
        radius1 = 5 * t1; // Raio da engrenagem 1
        radius2 = 5 * t2; // Raio da engrenagem 2

        // Calcular posições centradas e engrenadas (com folga)
        cy = 360; // Altura fixa no centro vertical
        int distance = radius1 + radius2 - 8; // Distância entre centros (-8 = folga)
        cx1 = 640 - distance / 2; // Centro da engrenagem 1
        cx2 = cx1 + distance; // Centro da engrenagem 2
    }

    void updateRotation(double rpm1, double rpm2) { // Atualiza o ângulo de rotação
        angle1 += rpm1 / 500; // Incrementa ângulo da engrenagem 1 proporcional ao RPM
        angle2 = -angle1 * ((double)teeth1 / teeth2) + Math.PI / teeth2; // Calcula rotação inversa da engrenagem 2
    }

    void setGraphData(ArrayList<Double> rpm, ArrayList<Double> torque) { // Define dados para o gráfico
        this.rpmHist = rpm; // Guarda histórico de RPM
        this.torqueHist = torque; // Guarda histórico de torque
    }

    protected void paintComponent(Graphics g) { // Método que desenha o painel
        super.paintComponent(g); // Limpa tela
        Graphics2D g2 = (Graphics2D) g; // Conversão para Graphics2D (melhor qualidade)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Suaviza linhas

        // Desenhar engrenagens engrenadas
        drawGear(g2, cx1, cy, radius1, teeth1, angle1, new Color(180, 180, 180)); // Engrenagem 1
        drawGear(g2, cx2, cy, radius2, teeth2, angle2, new Color(120, 120, 120)); // Engrenagem 2

        // Desenhar gráfico simples
        int gx = getWidth() - 400, gy = getHeight() - 200; // Posição do gráfico
        g2.setColor(Color.WHITE); // Cor branca para borda
        g2.drawRect(gx, gy, 350, 150); // Retângulo do gráfico
        g2.drawString("Verde = RPM | Vermelho = Torque", gx, gy - 5); // Legenda

        if (rpmHist != null) { // Se há dados
            for (int i = 1; i < rpmHist.size(); i++) { // Percorre pontos do histórico
                int x1 = gx + i; // Coordenada x anterior
                int x2 = gx + i + 1; // Coordenada x atual
                int y1 = gy + 150 - (int)(rpmHist.get(i-1) / 2); // Calcula y do ponto anterior (RPM)
                int y2 = gy + 150 - (int)(rpmHist.get(i) / 2); // Calcula y atual (RPM)
                g2.setColor(Color.GREEN); // Cor verde
                g2.drawLine(x1, y1, x2, y2); // Desenha linha do RPM

                int t1 = gy + 150 - (int)(torqueHist.get(i-1) * 3); // y torque anterior
                int t2 = gy + 150 - (int)(torqueHist.get(i) * 3); // y torque atual
                g2.setColor(Color.RED); // Cor vermelha
                g2.drawLine(x1, t1, x2, t2); // Desenha linha do torque
            }
        }
    }

    void drawGear(Graphics2D g2, int cx, int cy, int radius, int teeth, double angle, Color color) { // Função que desenha uma engrenagem
        g2.setColor(color); // Define a cor da engrenagem
        double toothAngle = 2 * Math.PI / teeth; // Ângulo de cada dente
        Polygon gear = new Polygon(); // Cria o polígono da engrenagem

        for (int i = 0; i < teeth; i++) { // Para cada dente
            double a1 = angle + i * toothAngle; // Início do dente
            double a2 = a1 + toothAngle / 2; // Meio do dente
            double a3 = a1 + toothAngle; // Fim do dente

            int innerX1 = cx + (int)(Math.cos(a1) * (radius - 8)); // Ponto interno 1
            int innerY1 = cy + (int)(Math.sin(a1) * (radius - 8)); // Ponto interno 1 (Y)

            int outerX = cx + (int)(Math.cos(a2) * (radius + 8)); // Ponto externo (ponta do dente)
            int outerY = cy + (int)(Math.sin(a2) * (radius + 8)); // Ponto externo (Y)

            int innerX2 = cx + (int)(Math.cos(a3) * (radius - 8)); // Ponto interno 2
            int innerY2 = cy + (int)(Math.sin(a3) * (radius - 8)); // Ponto interno 2 (Y)

            gear.addPoint(innerX1, innerY1); // Adiciona primeiro ponto ao polígono
            gear.addPoint(outerX, outerY); // Adiciona ponta do dente
            gear.addPoint(innerX2, innerY2); // Adiciona segundo ponto interno
        }

        g2.fillPolygon(gear); // Preenche o polígono (engrenagem)

        // Buraco central (eixo)
        g2.setColor(Color.DARK_GRAY); // Cor do eixo
        g2.fillOval(cx - radius / 5, cy - radius / 5, radius / 2, radius / 2); // Círculo no centro

        // Texto no centro
        g2.setColor(Color.WHITE); // Cor do texto
        g2.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte
        String text = teeth + " dentes"; // Texto com número de dentes
        FontMetrics fm = g2.getFontMetrics(); // Pega informações de tamanho da fonte
        g2.drawString(text, cx - fm.stringWidth(text) / 2, cy + fm.getAscent() / 2); // Centraliza o texto no centro da engrenagem
    }
}
