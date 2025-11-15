import javax.swing.*;
import java.awt.*;

public class MainFrame extends BaseFrame{
    int dentes1;
    double rpm1;
    double torque1;
    int dentes2;
    double rpm2;
    double torque2;
    int rpmSliderValue;
    int torqueSliderValue;
    double rpmOriginal1;
    double rpmOriginal2;
    double torqueOriginal1;
    double torqueOriginal2;

    Manager manager = new Manager();

    JLabel dentes1Label;
    JLabel rpm1Label;
    JLabel torque1Label;
    JLabel dentes2Label;
    JLabel rpm2Label;
    JLabel torque2Label;

    public MainFrame(int dentes1, double rpm1, double torque1, int dentes2, double rpm2, double torque2) {
        super("Simulação de Engrenagens", 1300, 800, Color.WHITE);

        this.dentes1 = dentes1;
        this.rpm1 = rpm1;
        this.torque1 = torque1;
        this.dentes2 = dentes2;
        this.rpm2 = rpm2;
        this.torque2 = torque2;

        this.rpmOriginal1 = rpm1;
        this.rpmOriginal2 = rpm2;
        this.torqueOriginal1 = torque1;
        this.torqueOriginal2 = torque2;

        JButton exitButton = new JButton("Sair da Simulação");
        exitButton.addActionListener(e -> sair());
        adicionarComponente(exitButton, 1065, 700, 200, 50);

        JButton restartButton = new JButton("Começar Novamente");
        restartButton.addActionListener(e -> reiniciar());
        adicionarComponente(restartButton, 1065, 635, 200, 50);

        mostrarInformacoes();
        adicionarSlideBar(rpm1Label, rpm2Label, torque1Label, torque2Label);
        setVisible(true);
    }

    public void mostrarInformacoes(){
        // Inicializa os labels da classe (atributos)
        dentes1Label = new JLabel("Dentes 1: " + dentes1);
        rpm1Label = new JLabel("RPM     1: " + rpm1);
        torque1Label = new JLabel("Torque 1: " + torque1);

        dentes2Label = new JLabel("Dentes 2: " + dentes2);
        rpm2Label = new JLabel("RPM     2: " + rpm2);
        torque2Label = new JLabel("Torque 2: " + torque2);

        // Engrenagem 1
        adicionarComponente(dentes1Label, 20, 20, 200, 25);
        adicionarComponente(rpm1Label, 20, 50, 200, 25);
        adicionarComponente(torque1Label, 20, 80, 200, 25);

        // Engrenagem 2
        adicionarComponente(dentes2Label, 20, 130, 200, 25);
        adicionarComponente(rpm2Label, 20, 160, 200, 25);
        adicionarComponente(torque2Label, 20, 190, 200, 25);

        // descrição sliders
        adicionarComponente(new JLabel("Controle de RPM"), 600, 685, 200, 25);
        adicionarComponente(new JLabel("Controle de Torque"), 75, 685, 200, 25);
    }


    public void adicionarSlideBar(JLabel rpm1Label1, JLabel rpm2Label1, JLabel torque1Label1, JLabel torque2Label1){
        JSlider rpmSlider = new JSlider(-300, 300, 0);
        JSlider torqueSlider = new JSlider(-300, 300, 0);
        rpmSlider.addChangeListener(e -> {
            int valor = rpmSlider.getValue();
            this.rpmSliderValue = valor;
            atualizarInformacoes(rpm1Label1, rpm2Label1, torque1Label1, torque2Label1);
        });
        torqueSlider.addChangeListener(e -> {
            int valor = torqueSlider.getValue();
            this.torqueSliderValue = valor;
            atualizarInformacoes(rpm1Label1, rpm2Label1, torque1Label1, torque2Label1);
        });
        adicionarComponente(rpmSlider, 250, 700, 800, 50);
        adicionarComponente(torqueSlider, 20, 700, 220, 50);
    }

    public void atualizarInformacoes(JLabel rpm1Label, JLabel rpm2Label, JLabel torque1Label, JLabel torque2Label){
        rpm1 = rpmOriginal1 * (1 + (rpmSliderValue / 100.0));
        rpm2 = rpmOriginal2 * (1 + (rpmSliderValue / 100.0));

        rpm1Label.setText("RPM     1: " + String.format("%.2f", rpm1));
        rpm2Label.setText("RPM     2: " + String.format("%.2f", rpm2));

        torque1 = torqueOriginal1 * (1 + (torqueSliderValue / 100.0));
        torque2 = torqueOriginal2 * (1 + (torqueSliderValue / 100.0));

        torque1Label.setText("Torque 1: " + String.format("%.2f", torque1));
        torque2Label.setText("Torque 2: " + String.format("%.2f", torque2));
    }

    public void sair(){
        this.dispose();
    }

    public void reiniciar(){
        this.dispose();
        ConfigFrame configFrame = new ConfigFrame();
    }
}
