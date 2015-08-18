package uk.co.qmunity.lib.client.gui.widget;

public interface IWidgetAction {

    public String getType();

    public void setCanceled(boolean canceled);

    public boolean isCanceled();

    public static class WidgetActionClick implements IWidgetAction {

        public final int x, y, button;

        public WidgetActionClick(int x, int y, int button) {

            this.x = x;
            this.y = y;
            this.button = button;
        }

        @Override
        public String getType() {

            return "click";
        }

        @Override
        public void setCanceled(boolean canceled) {

        }

        @Override
        public boolean isCanceled() {

            return false;
        }

    }

}
