public record Engrenagem(
        int numeroDeDentes,
        double RPM,
        double torque,
        double raio,
        int posX,
        int posY,
        double anguloInicial
) {
    @Override
    public int numeroDeDentes() {
        return numeroDeDentes;
    }

    @Override
    public double RPM() {
        return RPM;
    }

    @Override
    public double torque() {
        return torque;
    }

    @Override
    public double raio() {
        return raio;
    }

    @Override
    public int posX() {
        return posX;
    }

    @Override
    public int posY() {
        return posY;
    }

    @Override
    public double anguloInicial() {
        return anguloInicial;
    }
}
