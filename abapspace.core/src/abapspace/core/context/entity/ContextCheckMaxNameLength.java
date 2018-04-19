package abapspace.core.context.entity;

public class ContextCheckMaxNameLength {
    
    private Integer maxNameLength;
    private Integer actualNameLength;
    private Integer offset;
    private boolean valid;
    
    public ContextCheckMaxNameLength() {
    }

    public Integer getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(Integer maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public Integer getActualNameLength() {
        return actualNameLength;
    }

    public void setActualNameLength(Integer actualNameLength) {
        this.actualNameLength = actualNameLength;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
