package Backend;

import java.io.IOException;

import Frontend.WelcomeFrame;

public class Main {

    @SuppressWarnings("unused")
    public static void main(String args[]) throws IOException {
        WelcomeFrame frame = new WelcomeFrame();
        Simulador simulador = new Simulador();
        BackendController backendController = new BackendController(frame, simulador);
    }
}