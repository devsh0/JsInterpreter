package ast;

public abstract class BreakAndContinueSupportingBlock extends CompoundStatement {
    protected boolean breakFlag;
    protected boolean continueFlag;

    public abstract BreakAndContinueSupportingBlock setLabel(Identifier label);
    public abstract Identifier getLabel();

    public boolean setContinueFlag(boolean flag) {
        boolean oldFlag = this.continueFlag;
        this.continueFlag = flag;
        return oldFlag;
    }

    public boolean testAndClearContinueFlag() {
        if (continueFlag) {
            continueFlag = false;
            return true;
        }
        return false;
    }

    public boolean setBreakFlag(boolean flag) {
        boolean oldFlag = this.breakFlag;
        this.breakFlag = flag;
        return oldFlag;
    }

    public boolean testAndClearBreakFlag() {
        if (breakFlag) {
            breakFlag = false;
            return true;
        }
        return false;
    }
}
