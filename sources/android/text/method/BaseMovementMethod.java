package android.text.method;

import android.text.Layout;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class BaseMovementMethod implements MovementMethod {
    public boolean canSelectArbitrarily() {
        return false;
    }

    public void initialize(TextView widget, Spannable text) {
    }

    public boolean onKeyDown(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        boolean handled = handleMovementKey(widget, text, keyCode, getMovementMetaState(text, event), event);
        if (handled) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(text);
            MetaKeyKeyListener.resetLockedMeta(text);
        }
        return handled;
    }

    public boolean onKeyOther(TextView widget, Spannable text, KeyEvent event) {
        int movementMetaState = getMovementMetaState(text, event);
        int keyCode = event.getKeyCode();
        if (keyCode == 0 || event.getAction() != 2) {
            return false;
        }
        int repeat = event.getRepeatCount();
        boolean handled = false;
        for (int i = 0; i < repeat && handleMovementKey(widget, text, keyCode, movementMetaState, event); i++) {
            handled = true;
        }
        if (handled) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(text);
            MetaKeyKeyListener.resetLockedMeta(text);
        }
        return handled;
    }

    public boolean onKeyUp(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        return false;
    }

    public void onTakeFocus(TextView widget, Spannable text, int direction) {
    }

    public boolean onTouchEvent(TextView widget, Spannable text, MotionEvent event) {
        return false;
    }

    public boolean onTrackballEvent(TextView widget, Spannable text, MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(TextView widget, Spannable text, MotionEvent event) {
        if ((event.getSource() & 2) == 0 || event.getAction() != 8) {
            return false;
        }
        float vscroll;
        float hscroll;
        if ((event.getMetaState() & 1) != 0) {
            vscroll = 0.0f;
            hscroll = event.getAxisValue(9);
        } else {
            vscroll = -event.getAxisValue(9);
            hscroll = event.getAxisValue(10);
        }
        boolean handled = false;
        if (hscroll < 0.0f) {
            handled = false | scrollLeft(widget, text, (int) Math.ceil((double) (-hscroll)));
        } else if (hscroll > 0.0f) {
            handled = false | scrollRight(widget, text, (int) Math.ceil((double) hscroll));
        }
        if (vscroll < 0.0f) {
            handled |= scrollUp(widget, text, (int) Math.ceil((double) (-vscroll)));
        } else if (vscroll > 0.0f) {
            handled |= scrollDown(widget, text, (int) Math.ceil((double) vscroll));
        }
        return handled;
    }

    /* Access modifiers changed, original: protected */
    public int getMovementMetaState(Spannable buffer, KeyEvent event) {
        return KeyEvent.normalizeMetaState(MetaKeyKeyListener.getMetaState((CharSequence) buffer, event) & -1537) & -194;
    }

    /* Access modifiers changed, original: protected */
    public boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode, int movementMetaState, KeyEvent event) {
        if (keyCode != 92) {
            if (keyCode != 93) {
                if (keyCode != 122) {
                    if (keyCode != 123) {
                        switch (keyCode) {
                            case 19:
                                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                                    return up(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                                    return top(widget, buffer);
                                }
                                break;
                            case 20:
                                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                                    return down(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                                    return bottom(widget, buffer);
                                }
                                break;
                            case 21:
                                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                                    return left(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 4096)) {
                                    return leftWord(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                                    return lineStart(widget, buffer);
                                }
                                break;
                            case 22:
                                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                                    return right(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 4096)) {
                                    return rightWord(widget, buffer);
                                }
                                if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                                    return lineEnd(widget, buffer);
                                }
                                break;
                        }
                    } else if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                        return end(widget, buffer);
                    } else {
                        if (KeyEvent.metaStateHasModifiers(movementMetaState, 4096)) {
                            return bottom(widget, buffer);
                        }
                    }
                } else if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    return home(widget, buffer);
                } else {
                    if (KeyEvent.metaStateHasModifiers(movementMetaState, 4096)) {
                        return top(widget, buffer);
                    }
                }
            } else if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                return pageDown(widget, buffer);
            } else {
                if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                    return bottom(widget, buffer);
                }
            }
        } else if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
            return pageUp(widget, buffer);
        } else {
            if (KeyEvent.metaStateHasModifiers(movementMetaState, 2)) {
                return top(widget, buffer);
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean left(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean right(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean up(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean down(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean pageUp(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean pageDown(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean top(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean bottom(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean lineStart(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean lineEnd(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean leftWord(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean rightWord(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean home(TextView widget, Spannable buffer) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean end(TextView widget, Spannable buffer) {
        return false;
    }

    private int getTopLine(TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY());
    }

    private int getBottomLine(TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY() + getInnerHeight(widget));
    }

    private int getInnerWidth(TextView widget) {
        return (widget.getWidth() - widget.getTotalPaddingLeft()) - widget.getTotalPaddingRight();
    }

    private int getInnerHeight(TextView widget) {
        return (widget.getHeight() - widget.getTotalPaddingTop()) - widget.getTotalPaddingBottom();
    }

    private int getCharacterWidth(TextView widget) {
        return (int) Math.ceil((double) widget.getPaint().getFontSpacing());
    }

    private int getScrollBoundsLeft(TextView widget) {
        Layout layout = widget.getLayout();
        int topLine = getTopLine(widget);
        int bottomLine = getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        }
        int left = Integer.MAX_VALUE;
        for (int line = topLine; line <= bottomLine; line++) {
            int lineLeft = (int) Math.floor((double) layout.getLineLeft(line));
            if (lineLeft < left) {
                left = lineLeft;
            }
        }
        return left;
    }

    private int getScrollBoundsRight(TextView widget) {
        Layout layout = widget.getLayout();
        int topLine = getTopLine(widget);
        int bottomLine = getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        }
        int right = Integer.MIN_VALUE;
        for (int line = topLine; line <= bottomLine; line++) {
            int lineRight = (int) Math.ceil((double) layout.getLineRight(line));
            if (lineRight > right) {
                right = lineRight;
            }
        }
        return right;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollLeft(TextView widget, Spannable buffer, int amount) {
        int minScrollX = getScrollBoundsLeft(widget);
        int scrollX = widget.getScrollX();
        if (scrollX <= minScrollX) {
            return false;
        }
        widget.scrollTo(Math.max(scrollX - (getCharacterWidth(widget) * amount), minScrollX), widget.getScrollY());
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollRight(TextView widget, Spannable buffer, int amount) {
        int maxScrollX = getScrollBoundsRight(widget) - getInnerWidth(widget);
        int scrollX = widget.getScrollX();
        if (scrollX >= maxScrollX) {
            return false;
        }
        widget.scrollTo(Math.min((getCharacterWidth(widget) * amount) + scrollX, maxScrollX), widget.getScrollY());
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollUp(TextView widget, Spannable buffer, int amount) {
        Layout layout = widget.getLayout();
        int top = widget.getScrollY();
        int topLine = layout.getLineForVertical(top);
        if (layout.getLineTop(topLine) == top) {
            topLine--;
        }
        if (topLine < 0) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(Math.max((topLine - amount) + 1, 0)));
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollDown(TextView widget, Spannable buffer, int amount) {
        Layout layout = widget.getLayout();
        int innerHeight = getInnerHeight(widget);
        int bottom = widget.getScrollY() + innerHeight;
        int bottomLine = layout.getLineForVertical(bottom);
        if (layout.getLineTop(bottomLine + 1) < bottom + 1) {
            bottomLine++;
        }
        int limit = layout.getLineCount() - 1;
        if (bottomLine > limit) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(Math.min((bottomLine + amount) - 1, limit) + 1) - innerHeight);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollPageUp(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        int topLine = layout.getLineForVertical(widget.getScrollY() - getInnerHeight(widget));
        if (topLine < 0) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(topLine));
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollPageDown(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        int innerHeight = getInnerHeight(widget);
        int bottomLine = layout.getLineForVertical((widget.getScrollY() + innerHeight) + innerHeight);
        if (bottomLine > layout.getLineCount() - 1) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(bottomLine + 1) - innerHeight);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollTop(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        if (getTopLine(widget) < 0) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(0));
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollBottom(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        int lineCount = layout.getLineCount();
        if (getBottomLine(widget) > lineCount - 1) {
            return false;
        }
        Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(lineCount) - getInnerHeight(widget));
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollLineStart(TextView widget, Spannable buffer) {
        int minScrollX = getScrollBoundsLeft(widget);
        if (widget.getScrollX() <= minScrollX) {
            return false;
        }
        widget.scrollTo(minScrollX, widget.getScrollY());
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean scrollLineEnd(TextView widget, Spannable buffer) {
        int maxScrollX = getScrollBoundsRight(widget) - getInnerWidth(widget);
        if (widget.getScrollX() >= maxScrollX) {
            return false;
        }
        widget.scrollTo(maxScrollX, widget.getScrollY());
        return true;
    }
}
