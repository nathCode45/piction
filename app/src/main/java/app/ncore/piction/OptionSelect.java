package app.ncore.piction;

public class OptionSelect {
    private String text;
    private int iconId;
    private boolean selected;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getText() {
        return text;
    }

    public int getIconId() {
        return iconId;
    }

    public OptionSelect(String text, int iconId) {
        this.text = text;
        this.iconId = iconId;
    }
    public OptionSelect(String text, int iconId, Category category){
        this.category =category;
        this.text = text;
        this.iconId = iconId;
    }
    public void select(boolean select){
        this.selected = select;
    }

}
