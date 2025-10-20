import java.util.Scanner;

public class SistemaEngrenagens {
    Scanner sc = new Scanner(System.in);
    public void rodar(){
        boolean continuar = true;
        while (continuar){
            System.out.println("Pinhão - dentes, rpm, torque: ");
            int dentes1 = sc.nextInt();
            if(dentes1 <= 0){
                System.out.println("Erro de digitação! valores devem ser maiores que zero!");
                break;
            }
            double rpm1 = sc.nextDouble();
            double torque1 = sc.nextDouble();
            Engrenagem pinhao = new Engrenagem(dentes1, rpm1, torque1);

            System.out.println("Coroa - dentes: ");
            int dentes2 = sc.nextInt();
            if(dentes2 <= 0){
                System.out.println("Erro de digitação! valores devem ser maiores que zero!");
                break;
            }
            double rpm2 = (double )rpm1 * (dentes1 / dentes2);
            double torque2 = torque1 * (rpm2 / rpm1);

            Engrenagem coroa = new Engrenagem(dentes2, rpm2, torque2);

            double relacao = (double) coroa.dentes() / pinhao.dentes();
            double novaVelocidade = (double) pinhao.rpm() / relacao;
            double novoTorque = (double) pinhao.torque() * relacao;

            System.out.println("--------------------------------------");
            System.out.println("Relação: " + relacao);
            System.out.println("Velocidade da coroa: " + novaVelocidade + "rpm");
            System.out.println("Torque da coroa: " + novoTorque + "N.m");
            System.out.println("--------------------------------------");
            System.out.println("Fazer outra simulação? sim/não");
            String simounao = sc.next(); // lê apenas uma palavra

            if(!simounao.equalsIgnoreCase("s") && !simounao.equalsIgnoreCase("sim")){
                continuar = false;
                System.out.println("Obrigado pela presença!");
                System.exit(0);
            }
            System.out.println("----------Próxima Simulação----------");
        }
    }
}
