package uk.co.qmunity.lib.client.gui.widget.button;

import uk.co.qmunity.lib.client.gui.widget.IWidgetAction;

public abstract class WidgetActionButton implements IWidgetAction {

    public final int x, y, button;

    public WidgetActionButton(int x, int y, int button) {

        this.x = x;
        this.y = y;
        this.button = button;
    }

    public static class Press extends WidgetActionButton {

        public Press(int x, int y, int button) {

            super(x, y, button);
        }

        @Override
        public String getType() {

            return "btnPress";
        }

        @Override
        public void setCanceled(boolean canceled) {

        }

        @Override
        public boolean isCanceled() {

            return false;
        }

    }

    public static class Toggle extends WidgetActionButton {

        public final boolean state;

        private boolean canceled = false;

        public Toggle(int x, int y, int button, boolean state) {

            super(x, y, button);

            this.state = state;
        }

        @Override
        public String getType() {

            return "btnToggle";
        }

        @Override
        public void setCanceled(boolean canceled) {

            this.canceled = canceled;
        }

        @Override
        public boolean isCanceled() {

            return canceled;
        }

    }

    public static class Cycle extends WidgetActionButton {

        public final int oldValue, newValue;

        private boolean canceled = false;

        public Cycle(int x, int y, int button, int lastValue, int newValue) {

            super(x, y, button);

            this.oldValue = lastValue;
            this.newValue = newValue;
        }

        @Override
        public String getType() {

            return "btnCycle";
        }

        @Override
        public void setCanceled(boolean canceled) {

            this.canceled = canceled;
        }

        @Override
        public boolean isCanceled() {

            return canceled;
        }

    }

}
