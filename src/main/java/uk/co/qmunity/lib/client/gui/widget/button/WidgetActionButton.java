package uk.co.qmunity.lib.client.gui.widget.button;

import uk.co.qmunity.lib.client.gui.widget.IWidgetAction;

public abstract class WidgetActionButton implements IWidgetAction {

    public final int x, y;

    public WidgetActionButton(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public static class Press extends WidgetActionButton {

        public Press(int x, int y) {

            super(x, y);
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

        public Toggle(int x, int y, boolean state) {

            super(x, y);

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

        public Cycle(int x, int y, int lastValue, int newValue) {

            super(x, y);

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
