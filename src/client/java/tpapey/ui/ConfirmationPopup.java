package tpapey.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfirmationPopup extends Screen {
    private final Runnable onConfirm; // Acción al confirmar
    private final Runnable onCancel; // Acción al cancelar

    public ConfirmationPopup(Text title, Runnable onConfirm, Runnable onCancel) {
        super(title); // Título del popup
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Confirmar"),
                        button -> {
                    onConfirm.run();
                    this.close();
                })
                .dimensions(this.width / 2 - 75, this.height / 2 - 10, 150, 20)
                .build()
        );

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Cancelar"),
                        button -> {
                            onCancel.run();
                            this.close();
                        })
                .dimensions(this.width / 2 - 75, this.height / 2 + 15, 150, 20)
                .build()
        );
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(null); // Cierra el popup y vuelve al juego
    }

    @Override
    public boolean shouldPause() {
        return true; // Pausa el juego mientras el popup está abierto
    }
}
