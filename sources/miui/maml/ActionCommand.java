package miui.maml;

import android.app.MobileDataUtils;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.provider.AlarmClock;
import android.service.notification.Condition;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.search.Function;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.maml.NotifierManager.OnNotifyListener;
import miui.maml.ObjectFactory.ActionCommandFactory;
import miui.maml.SoundManager.Command;
import miui.maml.SoundManager.SoundOptions;
import miui.maml.data.Expression;
import miui.maml.data.IndexedVariable;
import miui.maml.data.VariableBinder;
import miui.maml.data.VariableNames;
import miui.maml.data.VariableType;
import miui.maml.data.Variables;
import miui.maml.elements.ElementGroup;
import miui.maml.elements.ScreenElement;
import miui.maml.util.IntentInfo;
import miui.maml.util.PreloadAppPolicyHelper;
import miui.maml.util.ReflectionHelper;
import miui.maml.util.Task;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import miui.maml.util.Variable;
import miui.maml.util.WifiApHelper;
import org.w3c.dom.Element;

public abstract class ActionCommand {
    private static final String COMMAND_BLUETOOTH = "Bluetooth";
    private static final String COMMAND_DATA = "Data";
    private static final String COMMAND_RING_MODE = "RingMode";
    private static final String COMMAND_USB_STORAGE = "UsbStorage";
    private static final String COMMAND_WIFI = "Wifi";
    private static final String LOG_TAG = "ActionCommand";
    private static final int STATE_DISABLED = 0;
    private static final int STATE_ENABLED = 1;
    private static final int STATE_INTERMEDIATE = 5;
    private static final int STATE_TURNING_OFF = 3;
    private static final int STATE_TURNING_ON = 2;
    private static final int STATE_UNKNOWN = 4;
    public static final String TAG_NAME = "Command";
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    protected ScreenElement mScreenElement;

    /* renamed from: miui.maml.ActionCommand$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType = new int[CommandType.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type = new int[Type.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType = new int[IntentType.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType = new int[TargetType.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ActionCommand$VariableBinderCommand$Command = new int[Command.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$SoundManager$Command = new int[Command.values().length];
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$VariableType = new int[VariableType.values().length];

        static {
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType[CommandType.PLAY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType[CommandType.PAUSE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType[CommandType.RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType[CommandType.PLAY_WITH_PARAMS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[TargetType.SCREEN_ELEMENT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[TargetType.VARIABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[TargetType.CONSTRUCTOR.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type[Type.PLAY.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type[Type.PAUSE.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type[Type.RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type[Type.PLAY_WITH_PARAMS.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Play.ordinal()] = 1;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Pause.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Resume.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$miui$maml$SoundManager$Command[Command.Stop.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType[IntentType.Activity.ordinal()] = 1;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType[IntentType.Broadcast.ordinal()] = 2;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType[IntentType.Service.ordinal()] = 3;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType[IntentType.Var.ordinal()] = 4;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$miui$maml$ActionCommand$VariableBinderCommand$Command[Command.Refresh.ordinal()] = 1;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.NUM.ordinal()] = 1;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.NUM_ARR.ordinal()] = 2;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.STR.ordinal()] = 3;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$miui$maml$data$VariableType[VariableType.STR_ARR.ordinal()] = 4;
            } catch (NoSuchFieldError e24) {
            }
        }
    }

    private static abstract class TargetCommand extends ActionCommand {
        protected String mLogStr;
        private Object mTarget;
        protected Expression mTargetIndex;
        protected String mTargetName;
        protected TargetType mTargetType;

        protected enum TargetType {
            SCREEN_ELEMENT,
            VARIABLE,
            CONSTRUCTOR
        }

        public TargetCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mTargetName = ele.getAttribute("target");
            if (TextUtils.isEmpty(this.mTargetName)) {
                this.mTargetName = null;
            }
            this.mTargetIndex = Expression.build(getVariables(), ele.getAttribute("targetIndex"));
            String type = ele.getAttribute("targetType");
            this.mTargetType = TargetType.SCREEN_ELEMENT;
            if ("element".equals(type)) {
                this.mTargetType = TargetType.SCREEN_ELEMENT;
            } else if ("var".equals(type)) {
                this.mTargetType = TargetType.VARIABLE;
            } else if ("ctor".equals(type)) {
                this.mTargetType = TargetType.CONSTRUCTOR;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("target=");
            stringBuilder.append(this.mTargetName);
            stringBuilder.append(" type=");
            stringBuilder.append(this.mTargetType.toString());
            this.mLogStr = stringBuilder.toString();
        }

        public void init() {
            super.init();
            int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[this.mTargetType.ordinal()];
            String str = "ActionCommand";
            if (i != 1) {
                if (i == 2) {
                    if (this.mTargetName != null) {
                        this.mTarget = Integer.valueOf(getVariables().registerVariable(this.mTargetName));
                    } else {
                        Log.e(str, "MethodCommand, type=var, empty target name");
                    }
                }
            } else if (this.mTarget == null) {
                ScreenElement targetElement = getRoot().findElement(this.mTargetName);
                this.mTarget = targetElement;
                StringBuilder stringBuilder;
                if (targetElement == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("could not find ScreenElement target, name: ");
                    stringBuilder.append(this.mTargetName);
                    Log.e(str, stringBuilder.toString());
                } else if (this.mTargetIndex != null && !ElementGroup.isArrayGroup(targetElement)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("target with index is not an ArrayGroup, name: ");
                    stringBuilder.append(this.mTargetName);
                    Log.e(str, stringBuilder.toString());
                    this.mTargetIndex = null;
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public Object getTarget() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[this.mTargetType.ordinal()];
            Object obj;
            if (i != 1) {
                if (i == 2 && this.mTarget != null) {
                    obj = getVariables().get(((Integer) this.mTarget).intValue());
                    if (this.mTargetIndex == null) {
                        return obj;
                    }
                    if (obj.getClass().isArray()) {
                        return Array.get(obj, (int) this.mTargetIndex.evaluate());
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("target with index is not an Array variable, name: ");
                    stringBuilder.append(this.mTargetName);
                    Log.e("ActionCommand", stringBuilder.toString());
                    this.mTargetIndex = null;
                }
                return null;
            }
            obj = this.mTarget;
            if (obj != null) {
                Expression expression = this.mTargetIndex;
                if (expression != null) {
                    return ((ElementGroup) obj).getChild((int) expression.evaluate());
                }
            }
            return this.mTarget;
        }
    }

    private static class ActionPerformCommand extends TargetCommand {
        public static final String TAG_NAME = "ActionCommand";
        private String mAction;
        private Expression mActionExp;

        public ActionPerformCommand(ScreenElement screenElement, Element ele) {
            super(screenElement, ele);
            this.mAction = ele.getAttribute("action");
            if (TextUtils.isEmpty(this.mAction)) {
                this.mAction = null;
                this.mActionExp = Expression.build(getVariables(), ele.getAttribute("actionExp"));
            }
        }

        public void doPerform() {
            ScreenElement target = (ScreenElement) getTarget();
            if (target != null) {
                String str = this.mAction;
                if (str != null) {
                    target.performAction(str);
                } else {
                    str = this.mActionExp;
                    if (str != null) {
                        str = str.evaluateStr();
                        if (str != null) {
                            target.performAction(str);
                        }
                    }
                }
            }
        }
    }

    private static class AnimationCommand extends TargetCommand {
        public static final String TAG_NAME = "AnimationCommand";
        private boolean mAllAni;
        private String[] mAniTags;
        private CommandType mCommand;
        private Expression[] mPlayParams;

        private enum CommandType {
            INVALID,
            PLAY,
            PAUSE,
            RESUME,
            PLAY_WITH_PARAMS
        }

        public AnimationCommand(ScreenElement screenElement, Element ele) {
            super(screenElement, ele);
            String command = ele.getAttribute("command");
            if (command.equalsIgnoreCase("play")) {
                this.mCommand = CommandType.PLAY;
            } else if (command.equalsIgnoreCase("pause")) {
                this.mCommand = CommandType.PAUSE;
            } else if (command.equalsIgnoreCase("resume")) {
                this.mCommand = CommandType.RESUME;
            } else if (command.toLowerCase().startsWith("play(") && command.endsWith(")")) {
                this.mCommand = CommandType.PLAY_WITH_PARAMS;
                this.mPlayParams = Expression.buildMultiple(getVariables(), command.substring(5, command.length() - 1));
                Expression[] expressionArr = this.mPlayParams;
                if (!(expressionArr == null || expressionArr.length == 2 || expressionArr.length == 4)) {
                    Log.e("ActionCommand", "bad expression format");
                }
            } else {
                this.mCommand = CommandType.INVALID;
            }
            String strTags = ele.getAttribute("tags");
            if (".".equals(strTags)) {
                this.mAllAni = true;
            } else if (!TextUtils.isEmpty(strTags)) {
                this.mAniTags = strTags.split(",");
            }
        }

        public void doPerform() {
            ScreenElement target = (ScreenElement) getTarget();
            if (target != null) {
                if ((this.mCommand == CommandType.PLAY || this.mCommand == CommandType.PLAY_WITH_PARAMS) && (this.mAllAni || this.mAniTags != null)) {
                    target.setAnim(this.mAniTags);
                }
                int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$AnimationCommand$CommandType[this.mCommand.ordinal()];
                if (i == 1) {
                    target.playAnim();
                } else if (i == 2) {
                    target.pauseAnim();
                } else if (i == 3) {
                    target.resumeAnim();
                } else if (i == 4) {
                    long startTime;
                    long endTime;
                    boolean isLoop;
                    boolean isDelay = false;
                    Expression[] expressionArr = this.mPlayParams;
                    boolean z = false;
                    if (expressionArr.length > 0) {
                        startTime = (long) (expressionArr[0] == null ? 0.0d : expressionArr[0].evaluate());
                    } else {
                        startTime = 0;
                    }
                    Expression[] expressionArr2 = this.mPlayParams;
                    if (expressionArr2.length > 1) {
                        long endTime2;
                        if (expressionArr2[1] == null) {
                            endTime2 = -4616189618054758400L;
                        } else {
                            endTime2 = expressionArr2[1].evaluate();
                        }
                        endTime = (long) endTime2;
                    } else {
                        endTime = -1;
                    }
                    expressionArr2 = this.mPlayParams;
                    if (expressionArr2.length > 2) {
                        boolean z2 = expressionArr2[2] != null && expressionArr2[2].evaluate() > 0.0d;
                        isLoop = z2;
                    } else {
                        isLoop = false;
                    }
                    Expression[] expressionArr3 = this.mPlayParams;
                    if (expressionArr3.length > 3) {
                        if (expressionArr3[3] != null && expressionArr3[3].evaluate() > 0.0d) {
                            z = true;
                        }
                        isDelay = z;
                    }
                    target.playAnim(startTime, endTime, isLoop, isDelay);
                }
            }
        }
    }

    @Deprecated
    public static abstract class PropertyCommand extends ActionCommand {
        protected ScreenElement mTargetElement;
        private Variable mTargetObj;

        protected PropertyCommand(ScreenElement screenElement, Variable targetObj, String value) {
            super(screenElement);
            this.mTargetObj = targetObj;
        }

        public static PropertyCommand create(ScreenElement screenElement, String target, String value) {
            Variable t = new Variable(target);
            if ("visibility".equals(t.getPropertyName())) {
                return new VisibilityProperty(screenElement, t, value);
            }
            if (AnimationProperty.PROPERTY_NAME.equals(t.getPropertyName())) {
                return new AnimationProperty(screenElement, t, value);
            }
            return null;
        }

        public void init() {
            super.init();
            if (this.mTargetObj != null && this.mTargetElement == null) {
                this.mTargetElement = getRoot().findElement(this.mTargetObj.getObjName());
                if (this.mTargetElement == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("could not find PropertyCommand target, name: ");
                    stringBuilder.append(this.mTargetObj.getObjName());
                    Log.w("ActionCommand", stringBuilder.toString());
                    this.mTargetObj = null;
                }
            }
        }

        public void perform() {
            if (this.mTargetElement != null) {
                doPerform();
            }
        }
    }

    @Deprecated
    private static class AnimationProperty extends PropertyCommand {
        public static final String PROPERTY_NAME = "animation";
        private Expression[] mPlayParams;
        private Type mType;

        enum Type {
            INVALID,
            PLAY,
            PAUSE,
            RESUME,
            PLAY_WITH_PARAMS
        }

        protected AnimationProperty(ScreenElement screenElement, Variable targetObj, String value) {
            super(screenElement, targetObj, value);
            if (value.equalsIgnoreCase("play")) {
                this.mType = Type.PLAY;
            } else if (value.equalsIgnoreCase("pause")) {
                this.mType = Type.PAUSE;
            } else if (value.equalsIgnoreCase("resume")) {
                this.mType = Type.RESUME;
            } else if (value.toLowerCase().startsWith("play(") && value.endsWith(")")) {
                this.mType = Type.PLAY_WITH_PARAMS;
                this.mPlayParams = Expression.buildMultiple(getVariables(), value.substring(5, value.length() - 1));
                Expression[] expressionArr = this.mPlayParams;
                if (expressionArr != null && expressionArr.length != 2 && expressionArr.length != 4) {
                    Log.e("ActionCommand", "bad expression format");
                }
            } else {
                this.mType = Type.INVALID;
            }
        }

        public void doPerform() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$AnimationProperty$Type[this.mType.ordinal()];
            if (i == 1) {
                this.mTargetElement.playAnim();
            } else if (i == 2) {
                this.mTargetElement.pauseAnim();
            } else if (i == 3) {
                this.mTargetElement.resumeAnim();
            } else if (i == 4) {
                long startTime = 0;
                long endTime = -1;
                boolean isLoop = false;
                boolean isDelay = false;
                Expression[] expressionArr = this.mPlayParams;
                boolean z = false;
                if (expressionArr.length > 0) {
                    startTime = (long) (expressionArr[0] == null ? 0.0d : expressionArr[0].evaluate());
                }
                expressionArr = this.mPlayParams;
                if (expressionArr.length > 1) {
                    double d;
                    if (expressionArr[1] == null) {
                        d = -1.0d;
                    } else {
                        d = expressionArr[1].evaluate();
                    }
                    endTime = (long) d;
                }
                expressionArr = this.mPlayParams;
                if (expressionArr.length > 2) {
                    boolean z2 = expressionArr[2] != null && expressionArr[2].evaluate() > 0.0d;
                    isLoop = z2;
                }
                Expression[] expressionArr2 = this.mPlayParams;
                if (expressionArr2.length > 3) {
                    if (expressionArr2[3] != null && expressionArr2[3].evaluate() > 0.0d) {
                        z = true;
                    }
                    isDelay = z;
                }
                this.mTargetElement.playAnim(startTime, endTime, isLoop, isDelay);
            }
        }
    }

    private static abstract class BaseMethodCommand extends TargetCommand {
        protected static final int ERROR_EXCEPTION = -2;
        protected static final int ERROR_NO_METHOD = -1;
        protected static final int ERROR_SUCCESS = 1;
        protected IndexedVariable mErrorCodeVar;
        private ObjVar[] mParamObjVars;
        protected Class<?>[] mParamTypes;
        protected Object[] mParamValues;
        private Expression[] mParams;
        protected IndexedVariable mReturnVar;
        protected Class<?> mTargetClass;
        protected String mTargetClassName;

        public BaseMethodCommand(ScreenElement screenElement, Element ele) {
            String str = "ActionCommand";
            super(screenElement, ele);
            this.mTargetClassName = ele.getAttribute(Function.CLASS);
            if (TextUtils.isEmpty(this.mTargetClassName)) {
                this.mTargetClassName = null;
            }
            this.mParams = Expression.buildMultiple(getVariables(), ele.getAttribute("params"));
            String paramTypes = ele.getAttribute("paramTypes");
            if (!(this.mParams == null || TextUtils.isEmpty(paramTypes))) {
                try {
                    this.mParamTypes = ReflectionHelper.strTypesToClass(TextUtils.split(paramTypes, ","));
                    if (this.mParams.length != this.mParamTypes.length) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.mLogStr);
                        stringBuilder.append("different length of params and paramTypes");
                        Log.e(str, stringBuilder.toString());
                        this.mParams = null;
                        this.mParamTypes = null;
                    }
                } catch (ClassNotFoundException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.mLogStr);
                    stringBuilder2.append("invalid method paramTypes. ");
                    stringBuilder2.append(e.toString());
                    Log.e(str, stringBuilder2.toString());
                    this.mParams = null;
                    this.mParamTypes = null;
                }
            }
            str = ele.getAttribute("return");
            if (!TextUtils.isEmpty(str)) {
                this.mReturnVar = new IndexedVariable(str, getVariables(), VariableType.parseType(ele.getAttribute("returnType")).isNumber());
            }
            String errorVar = ele.getAttribute("errorVar");
            if (!TextUtils.isEmpty(errorVar)) {
                this.mErrorCodeVar = new IndexedVariable(errorVar, getVariables(), true);
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(this.mLogStr);
            stringBuilder3.append(", class=");
            stringBuilder3.append(this.mTargetClassName);
            stringBuilder3.append(" type=");
            stringBuilder3.append(this.mTargetType.toString());
            this.mLogStr = stringBuilder3.toString();
        }

        public void init() {
            super.init();
            Class[] clsArr = this.mParamTypes;
            if (clsArr != null) {
                if (this.mParamObjVars == null) {
                    this.mParamObjVars = new ObjVar[clsArr.length];
                }
                int i = 0;
                while (true) {
                    Class<?> c = this.mParamTypes;
                    if (i >= c.length) {
                        break;
                    }
                    this.mParamObjVars[i] = null;
                    c = c[i];
                    if ((!c.isPrimitive() || c.isArray()) && c != String.class) {
                        Expression exp = this.mParams[i];
                        if (exp != null) {
                            String name = exp.evaluateStr();
                            if (!TextUtils.isEmpty(name)) {
                                this.mParamObjVars[i] = new ObjVar(name, getVariables());
                            }
                        }
                    }
                    i++;
                }
            }
            String str = this.mTargetClassName;
            if (str != null) {
                try {
                    this.mTargetClass = Class.forName(str);
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("target class not found, name: ");
                    stringBuilder.append(this.mTargetClassName);
                    stringBuilder.append("\n");
                    stringBuilder.append(e.toString());
                    Log.w("ActionCommand", stringBuilder.toString());
                }
            }
        }

        public void finish() {
            super.finish();
            this.mParamValues = null;
        }

        /* Access modifiers changed, original: protected */
        public void prepareParams() {
            Expression[] expressionArr = this.mParams;
            if (expressionArr != null) {
                if (this.mParamValues == null) {
                    this.mParamValues = new Object[expressionArr.length];
                }
                int i = 0;
                while (true) {
                    Expression expression = this.mParams;
                    if (i < expression.length) {
                        Object[] objArr = this.mParamValues;
                        Object obj = null;
                        objArr[i] = null;
                        Class<?> paraClass = this.mParamTypes[i];
                        expression = expression[i];
                        if (expression != null) {
                            if (paraClass == String.class) {
                                objArr[i] = expression.evaluateStr();
                            } else if (paraClass == Integer.TYPE) {
                                this.mParamValues[i] = Integer.valueOf((int) ((long) expression.evaluate()));
                            } else if (paraClass == Long.TYPE) {
                                this.mParamValues[i] = Long.valueOf((long) expression.evaluate());
                            } else if (paraClass == Boolean.TYPE) {
                                this.mParamValues[i] = Boolean.valueOf(expression.evaluate() > 0.0d);
                            } else if (paraClass == Double.TYPE) {
                                this.mParamValues[i] = Double.valueOf(expression.evaluate());
                            } else if (paraClass == Float.TYPE) {
                                this.mParamValues[i] = Float.valueOf((float) expression.evaluate());
                            } else if (paraClass == Byte.TYPE) {
                                this.mParamValues[i] = Byte.valueOf((byte) ((int) ((long) expression.evaluate())));
                            } else if (paraClass == Short.TYPE) {
                                this.mParamValues[i] = Short.valueOf((short) ((int) ((long) expression.evaluate())));
                            } else if (paraClass == Character.TYPE) {
                                this.mParamValues[i] = Character.valueOf((char) ((int) ((long) expression.evaluate())));
                            } else {
                                ObjVar objVar = this.mParamObjVars[i];
                                Object[] objArr2 = this.mParamValues;
                                if (objVar != null) {
                                    obj = objVar.get();
                                }
                                objArr2[i] = obj;
                            }
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private static abstract class StatefulActionCommand extends ActionCommand {
        private IndexedVariable mVar;

        public StatefulActionCommand(ScreenElement screenElement, String stateName) {
            super(screenElement);
            this.mVar = new IndexedVariable(stateName, getVariables(), true);
        }

        /* Access modifiers changed, original: protected|final */
        public final void updateState(int state) {
            IndexedVariable indexedVariable = this.mVar;
            if (indexedVariable != null) {
                indexedVariable.set((double) state);
                getRoot().requestUpdate();
            }
        }
    }

    private static abstract class NotificationReceiver extends StatefulActionCommand implements OnNotifyListener {
        private NotifierManager mNotifierManager = NotifierManager.getInstance(getContext());
        private String mType;

        public abstract void update();

        public NotificationReceiver(ScreenElement screenElement, String stateName, String type) {
            super(screenElement, stateName);
            this.mType = type;
        }

        public void onNotify(Context context, Intent intent, Object o) {
            asyncUpdate();
        }

        /* Access modifiers changed, original: protected */
        public void asyncUpdate() {
            ActionCommand.mHandler.post(new Runnable() {
                public void run() {
                    NotificationReceiver.this.update();
                }
            });
        }

        public void init() {
            update();
            this.mNotifierManager.acquireNotifier(this.mType, this);
        }

        public void pause() {
            this.mNotifierManager.pause(this.mType, this);
        }

        public void resume() {
            update();
            this.mNotifierManager.resume(this.mType, this);
        }

        public void finish() {
            this.mNotifierManager.releaseNotifier(this.mType, this);
        }
    }

    private static class BluetoothSwitchCommand extends NotificationReceiver {
        private BluetoothAdapter mBluetoothAdapter;
        private boolean mBluetoothEnable;
        private boolean mBluetoothEnabling;
        private OnOffCommandHelper mOnOffHelper;

        public BluetoothSwitchCommand(ScreenElement screenElement, String value) {
            super(screenElement, "bluetooth_state", BluetoothAdapter.ACTION_STATE_CHANGED);
            this.mOnOffHelper = new OnOffCommandHelper(value);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            if (ensureBluetoothAdapter()) {
                if (this.mOnOffHelper.mIsToggle) {
                    if (this.mBluetoothEnable) {
                        this.mBluetoothAdapter.disable();
                        this.mBluetoothEnabling = false;
                    } else {
                        this.mBluetoothAdapter.enable();
                        this.mBluetoothEnabling = true;
                    }
                } else if (!(this.mBluetoothEnabling || this.mBluetoothEnable == this.mOnOffHelper.mIsOn)) {
                    if (this.mOnOffHelper.mIsOn) {
                        this.mBluetoothAdapter.enable();
                        this.mBluetoothEnabling = true;
                    } else {
                        this.mBluetoothAdapter.disable();
                        this.mBluetoothEnabling = false;
                    }
                }
                update();
            }
        }

        /* Access modifiers changed, original: protected */
        public void update() {
            if (ensureBluetoothAdapter()) {
                this.mBluetoothEnable = this.mBluetoothAdapter.isEnabled();
                int i = 0;
                if (this.mBluetoothEnable) {
                    this.mBluetoothEnabling = false;
                    updateState(1);
                } else {
                    if (this.mBluetoothEnabling) {
                        i = 2;
                    }
                    updateState(i);
                }
            }
        }

        private boolean ensureBluetoothAdapter() {
            if (this.mBluetoothAdapter == null) {
                this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            return this.mBluetoothAdapter != null;
        }
    }

    private static class ConditionCommand extends ActionCommand {
        private ActionCommand mCommand;
        private Expression mCondition;

        public ConditionCommand(ActionCommand command, Expression condition) {
            super(command.getRoot());
            this.mCommand = command;
            this.mCondition = condition;
        }

        public void init() {
            this.mCommand.init();
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            if (this.mCondition.evaluate() > 0.0d) {
                this.mCommand.perform();
            }
        }
    }

    private static class DataSwitchCommand extends NotificationReceiver {
        private boolean mApnEnable;
        private MobileDataUtils mMobileDataUtils = MobileDataUtils.getInstance();
        private OnOffCommandHelper mOnOffHelper;

        public DataSwitchCommand(ScreenElement screenElement, String value) {
            super(screenElement, VariableNames.DATA_STATE, NotifierManager.TYPE_MOBILE_DATA);
            this.mOnOffHelper = new OnOffCommandHelper(value);
        }

        /* Access modifiers changed, original: protected */
        public void update() {
            this.mApnEnable = this.mMobileDataUtils.isMobileEnable(this.mScreenElement.getContext().mContext);
            updateState(this.mApnEnable);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            boolean enable = this.mApnEnable;
            if (this.mOnOffHelper.mIsToggle) {
                enable = this.mApnEnable ^ 1;
            } else {
                enable = this.mOnOffHelper.mIsOn;
            }
            if (this.mApnEnable != enable) {
                this.mMobileDataUtils.enableMobileData(this.mScreenElement.getContext().mContext, enable);
            }
        }
    }

    private static class DelayCommand extends ActionCommand {
        private Runnable mCmd = new Runnable() {
            public void run() {
                DelayCommand.this.mCommand.perform();
            }
        };
        private ActionCommand mCommand;
        private long mDelay;

        public DelayCommand(ActionCommand command, long delay) {
            super(command.getRoot());
            this.mCommand = command;
            this.mDelay = delay;
        }

        public void init() {
            this.mCommand.init();
        }

        public void finish() {
            getRoot().removeCallbacks(this.mCmd);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            getRoot().postDelayed(this.mCmd, this.mDelay);
        }
    }

    private static class ExternCommand extends ActionCommand {
        public static final String TAG_NAME = "ExternCommand";
        private String mCommand;
        private Expression mNumParaExp;
        private Expression mStrParaExp;

        public ExternCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mCommand = ele.getAttribute("command");
            this.mNumParaExp = Expression.build(getVariables(), ele.getAttribute("numPara"));
            this.mStrParaExp = Expression.build(getVariables(), ele.getAttribute("strPara"));
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            ScreenElementRoot root = getRoot();
            String str = this.mCommand;
            Expression expression = this.mNumParaExp;
            String str2 = null;
            Double valueOf = expression == null ? null : Double.valueOf(expression.evaluate());
            Expression expression2 = this.mStrParaExp;
            if (expression2 != null) {
                str2 = expression2.evaluateStr();
            }
            root.issueExternCommand(str, valueOf, str2);
        }
    }

    private static class FieldCommand extends BaseMethodCommand {
        public static final String TAG_NAME = "FieldCommand";
        private Field mField;
        private String mFieldName;
        private boolean mIsSet;

        public FieldCommand(ScreenElement screenElement, Element ele) {
            super(screenElement, ele);
            this.mFieldName = ele.getAttribute("field");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FieldCommand, ");
            stringBuilder.append(this.mLogStr);
            stringBuilder.append(", field=");
            stringBuilder.append(this.mFieldName);
            stringBuilder.append("\n");
            this.mLogStr = stringBuilder.toString();
            String method = ele.getAttribute(RemindersColumns.METHOD);
            if ("get".equals(method)) {
                this.mIsSet = false;
            } else if ("set".equals(method)) {
                this.mIsSet = true;
            }
        }

        public void init() {
            super.init();
            int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[this.mTargetType.ordinal()];
            if ((i == 1 || i == 2) && this.mField != null) {
                loadField();
            }
        }

        /* Access modifiers changed, original: protected */
        public void loadField() {
            if (this.mTargetClass == null) {
                Object target = getTarget();
                if (target != null) {
                    this.mTargetClass = target.getClass();
                }
            }
            String str = "ActionCommand";
            if (this.mTargetClass != null) {
                try {
                    this.mField = this.mTargetClass.getField(this.mFieldName);
                    return;
                } catch (NoSuchFieldException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mLogStr);
                    stringBuilder.append(e.toString());
                    Log.e(str, stringBuilder.toString());
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(this.mLogStr);
            stringBuilder2.append("class is null.");
            Log.e(str, stringBuilder2.toString());
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            String str = "ActionCommand";
            if (this.mField == null) {
                loadField();
            }
            if (this.mField != null) {
                try {
                    int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[this.mTargetType.ordinal()];
                    if (i != 1 && i != 2) {
                        return;
                    }
                    if (this.mIsSet) {
                        prepareParams();
                        if (this.mParamValues != null && this.mParamValues.length == 1) {
                            this.mField.set(getTarget(), this.mParamValues[0]);
                        }
                    } else if (this.mReturnVar != null) {
                        this.mReturnVar.set(this.mField.get(getTarget()));
                    }
                } catch (IllegalArgumentException e) {
                    Log.e(str, e.toString());
                } catch (IllegalAccessException e2) {
                    Log.e(str, e2.toString());
                } catch (NullPointerException e3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mLogStr);
                    stringBuilder.append("Field target is null. ");
                    stringBuilder.append(e3.toString());
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
    }

    private static class IntentCommand extends ActionCommand {
        private static final String TAG_FALLBACK = "Fallback";
        public static final String TAG_NAME = "IntentCommand";
        private ObjVar mActivityOptionsBundle;
        private CommandTrigger mFallbackTrigger;
        private int mFlags;
        private Intent mIntent;
        private IntentInfo mIntentInfo;
        private IntentType mIntentType = IntentType.Activity;
        private IndexedVariable mIntentVar;

        private enum IntentType {
            Activity,
            Broadcast,
            Service,
            Var
        }

        public IntentCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mIntentInfo = new IntentInfo(ele, getVariables());
            String str = "broadcast";
            boolean isBroadcast = Boolean.parseBoolean(ele.getAttribute(str));
            String intentType = ele.getAttribute("type");
            if (isBroadcast || str.equals(intentType)) {
                this.mIntentType = IntentType.Broadcast;
            } else if ("service".equals(intentType)) {
                this.mIntentType = IntentType.Service;
            } else if (Context.ACTIVITY_SERVICE.equals(intentType)) {
                this.mIntentType = IntentType.Activity;
            } else if ("var".equals(intentType)) {
                this.mIntentType = IntentType.Var;
                str = ele.getAttribute("intentVar");
                if (!TextUtils.isEmpty(str)) {
                    this.mIntentVar = new IndexedVariable(str, getVariables(), false);
                }
            }
            this.mFlags = Utils.getAttrAsInt(ele, "flags", -1);
            str = ele.getAttribute("activityOption");
            this.mActivityOptionsBundle = TextUtils.isEmpty(str) ? null : new ObjVar(str, getVariables());
            Element node = Utils.getChild(ele, TAG_FALLBACK);
            if (node != null) {
                this.mFallbackTrigger = new CommandTrigger(node, screenElement);
            }
        }

        public void init() {
            Task configTask = getRoot().findTask(this.mIntentInfo.getId());
            if (!(configTask == null || TextUtils.isEmpty(configTask.action))) {
                this.mIntentInfo.set(configTask);
            }
            if (!Utils.isProtectedIntent(this.mIntentInfo.getAction())) {
                this.mIntent = new Intent();
                this.mIntentInfo.update(this.mIntent);
                int i = this.mFlags;
                if (i != -1) {
                    this.mIntent.setFlags(i);
                } else if (this.mIntentType == IntentType.Activity) {
                    this.mIntent.setFlags(872415232);
                }
                CommandTrigger commandTrigger = this.mFallbackTrigger;
                if (commandTrigger != null) {
                    commandTrigger.init();
                }
            }
        }

        public void finish() {
            CommandTrigger commandTrigger = this.mFallbackTrigger;
            if (commandTrigger != null) {
                commandTrigger.finish();
            }
        }

        public void pause() {
            CommandTrigger commandTrigger = this.mFallbackTrigger;
            if (commandTrigger != null) {
                commandTrigger.pause();
            }
        }

        public void resume() {
            CommandTrigger commandTrigger = this.mFallbackTrigger;
            if (commandTrigger != null) {
                commandTrigger.resume();
            }
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            Intent intent = this.mIntent;
            if (intent != null) {
                this.mIntentInfo.update(intent);
                String packageName;
                try {
                    int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$IntentCommand$IntentType[this.mIntentType.ordinal()];
                    if (i == 1) {
                        Bundle bd = this.mActivityOptionsBundle != null ? (Bundle) this.mActivityOptionsBundle.get() : null;
                        List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(this.mIntent, 65536);
                        if (list == null || list.size() <= 0) {
                            if (!TextUtils.isEmpty(this.mIntent.getPackage())) {
                                packageName = this.mIntent.getPackage();
                            } else if (!TextUtils.isEmpty(this.mIntent.getComponent().getPackageName())) {
                                packageName = this.mIntent.getComponent().getPackageName();
                            } else {
                                return;
                            }
                            PreloadAppPolicyHelper.installPreloadedDataApp(getContext(), packageName, this.mIntent, bd);
                        } else {
                            Utils.startActivity(getContext(), this.mIntent, bd);
                        }
                    } else if (i == 2) {
                        Utils.sendBroadcast(getContext(), this.mIntent);
                    } else if (i == 3) {
                        Utils.startService(getContext(), this.mIntent);
                    } else if (i == 4) {
                        if (this.mIntentVar != null) {
                            this.mIntentVar.set(this.mIntent);
                        }
                    }
                } catch (Exception e) {
                    packageName = "ActionCommand";
                    if (this.mFallbackTrigger != null) {
                        Log.i(packageName, "fail to send Intent, fallback...");
                        this.mFallbackTrigger.perform();
                    } else {
                        Log.e(packageName, e.toString());
                    }
                }
            }
        }
    }

    private static class MultiCommand extends ActionCommand {
        public static final String TAG_NAME = "MultiCommand";
        public static final String TAG_NAME1 = "GroupCommand";
        protected ArrayList<ActionCommand> mCommands = new ArrayList();

        public MultiCommand(final ScreenElement screenElement, Element ele) {
            super(screenElement);
            Utils.traverseXmlElementChildren(ele, null, new XmlTraverseListener() {
                public void onChild(Element child) {
                    ActionCommand command = ActionCommand.create(child, screenElement);
                    if (command != null) {
                        MultiCommand.this.mCommands.add(command);
                    }
                }
            });
        }

        public void init() {
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).init();
            }
        }

        public void finish() {
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).finish();
            }
        }

        public void pause() {
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).pause();
            }
        }

        public void resume() {
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).resume();
            }
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            Iterator it = this.mCommands.iterator();
            while (it.hasNext()) {
                ((ActionCommand) it.next()).perform();
            }
        }
    }

    private static class LoopCommand extends MultiCommand {
        private static final long COUNT_WARNING = 10000;
        public static final String TAG_NAME = "LoopCommand";
        private Expression mBeginExp;
        private Expression mConditionExp;
        private Expression mCountExp;
        private Expression mEndExp;
        private IndexedVariable mIndexVar;

        public LoopCommand(ScreenElement screenElement, Element ele) {
            super(screenElement, ele);
            String indexName = ele.getAttribute("indexName");
            Variables variables = getVariables();
            if (!TextUtils.isEmpty(indexName)) {
                this.mIndexVar = new IndexedVariable(indexName, variables, true);
            }
            this.mBeginExp = Expression.build(variables, ele.getAttribute("begin"));
            this.mCountExp = Expression.build(variables, ele.getAttribute("count"));
            if (this.mCountExp == null) {
                this.mEndExp = Expression.build(variables, ele.getAttribute("end"));
            }
            this.mConditionExp = Expression.build(variables, ele.getAttribute("loopCondition"));
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            Expression expression = this.mBeginExp;
            int end = 0;
            int begin = expression == null ? 0 : (int) expression.evaluate();
            Expression expression2 = this.mCountExp;
            if (expression2 != null) {
                end = (((int) expression2.evaluate()) + begin) - 1;
            } else {
                expression2 = this.mEndExp;
                if (expression2 != null) {
                    end = (int) expression2.evaluate();
                }
            }
            if (((long) (end - begin)) > 10000) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("count is too large: ");
                stringBuilder.append(end - begin);
                stringBuilder.append(", exceeds WARNING ");
                stringBuilder.append(10000);
                Log.w("ActionCommand", stringBuilder.toString());
            }
            int idx = begin;
            while (idx <= end) {
                Expression expression3 = this.mConditionExp;
                if (expression3 == null || expression3.evaluate() > 0.0d) {
                    IndexedVariable indexedVariable = this.mIndexVar;
                    if (indexedVariable != null) {
                        indexedVariable.set((double) idx);
                    }
                    int N = this.mCommands.size();
                    for (int i = 0; i < N; i++) {
                        ((ActionCommand) this.mCommands.get(i)).perform();
                    }
                    idx++;
                } else {
                    return;
                }
            }
        }
    }

    private static class MethodCommand extends BaseMethodCommand {
        public static final String TAG_NAME = "MethodCommand";
        private Constructor<?> mCtor;
        private Method mMethod;
        private String mMethodName;

        public MethodCommand(ScreenElement screenElement, Element ele) {
            super(screenElement, ele);
            this.mMethodName = ele.getAttribute(RemindersColumns.METHOD);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MethodCommand, ");
            stringBuilder.append(this.mLogStr);
            stringBuilder.append(", method=");
            stringBuilder.append(this.mMethodName);
            stringBuilder.append("\n    ");
            this.mLogStr = stringBuilder.toString();
        }

        public void init() {
            super.init();
            int i = AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType[this.mTargetType.ordinal()];
            if (i == 1 || i == 2) {
                if (this.mMethod == null) {
                    loadMethod();
                }
            } else if (i == 3) {
                if (!getRoot().getCapability(4)) {
                    this.mCtor = null;
                } else if (this.mCtor == null) {
                    String str = "ActionCommand";
                    if (this.mTargetClass != null) {
                        try {
                            this.mCtor = this.mTargetClass.getConstructor(this.mParamTypes);
                            return;
                        } catch (NoSuchMethodException e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(this.mLogStr);
                            stringBuilder.append("init, fail to find method. ");
                            stringBuilder.append(e.toString());
                            Log.e(str, stringBuilder.toString());
                            return;
                        }
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.mLogStr);
                    stringBuilder2.append("init, class is null.");
                    Log.e(str, stringBuilder2.toString());
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void loadMethod() {
            if (this.mTargetClass == null) {
                Object target = getTarget();
                if (target != null) {
                    this.mTargetClass = target.getClass();
                }
            }
            String str = "ActionCommand";
            StringBuilder stringBuilder;
            if (this.mTargetClass != null) {
                try {
                    this.mMethod = this.mTargetClass.getMethod(this.mMethodName, this.mParamTypes);
                } catch (NoSuchMethodException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.mLogStr);
                    stringBuilder2.append("loadMethod(). ");
                    stringBuilder2.append(e.toString());
                    Log.e(str, stringBuilder2.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mLogStr);
                stringBuilder.append("loadMethod(), successful.  ");
                stringBuilder.append(this.mMethod.toString());
                Log.d(str, stringBuilder.toString());
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mLogStr);
            stringBuilder.append("loadMethod(), class is null.");
            Log.e(str, stringBuilder.toString());
        }

        /* Access modifiers changed, original: protected */
        /* JADX WARNING: Failed to extract finally block: empty outs */
        public void doPerform() {
            /*
            r7 = this;
            r7.prepareParams();
            r0 = 0;
            r1 = 0;
            r2 = miui.maml.ActionCommand.AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$TargetCommand$TargetType;	 Catch:{ Exception -> 0x005c }
            r3 = r7.mTargetType;	 Catch:{ Exception -> 0x005c }
            r3 = r3.ordinal();	 Catch:{ Exception -> 0x005c }
            r2 = r2[r3];	 Catch:{ Exception -> 0x005c }
            r3 = 1;
            if (r2 == r3) goto L_0x002a;
        L_0x0012:
            r3 = 2;
            if (r2 == r3) goto L_0x002a;
        L_0x0015:
            r3 = 3;
            if (r2 == r3) goto L_0x0019;
        L_0x0018:
            goto L_0x0046;
        L_0x0019:
            r2 = r7.mCtor;	 Catch:{ Exception -> 0x005c }
            if (r2 == 0) goto L_0x0028;
        L_0x001d:
            r2 = r7.mCtor;	 Catch:{ Exception -> 0x005c }
            r3 = r7.mParamValues;	 Catch:{ Exception -> 0x005c }
            r2 = r2.newInstance(r3);	 Catch:{ Exception -> 0x005c }
            r1 = r2;
            r0 = 1;
            goto L_0x0046;
        L_0x0028:
            r0 = -1;
            goto L_0x0046;
        L_0x002a:
            r2 = r7.mMethod;	 Catch:{ Exception -> 0x005c }
            if (r2 != 0) goto L_0x0031;
        L_0x002e:
            r7.loadMethod();	 Catch:{ Exception -> 0x005c }
        L_0x0031:
            r2 = r7.mMethod;	 Catch:{ Exception -> 0x005c }
            if (r2 == 0) goto L_0x0044;
        L_0x0035:
            r2 = r7.getTarget();	 Catch:{ Exception -> 0x005c }
            r3 = r7.mMethod;	 Catch:{ Exception -> 0x005c }
            r4 = r7.mParamValues;	 Catch:{ Exception -> 0x005c }
            r3 = r3.invoke(r2, r4);	 Catch:{ Exception -> 0x005c }
            r1 = r3;
            r0 = 1;
            goto L_0x0046;
        L_0x0044:
            r0 = -1;
        L_0x0046:
            r2 = r7.mReturnVar;	 Catch:{ Exception -> 0x005c }
            if (r2 == 0) goto L_0x004f;
        L_0x004a:
            r2 = r7.mReturnVar;	 Catch:{ Exception -> 0x005c }
            r2.set(r1);	 Catch:{ Exception -> 0x005c }
        L_0x004f:
            r1 = r7.mErrorCodeVar;
            if (r1 == 0) goto L_0x009e;
        L_0x0053:
            r1 = r7.mErrorCodeVar;
            r2 = (double) r0;
            r1.set(r2);
            goto L_0x009e;
        L_0x005a:
            r1 = move-exception;
            goto L_0x009f;
        L_0x005c:
            r1 = move-exception;
            r2 = r1.getCause();	 Catch:{ all -> 0x005a }
            r3 = "ActionCommand";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
            r4.<init>();	 Catch:{ all -> 0x005a }
            r5 = r7.mLogStr;	 Catch:{ all -> 0x005a }
            r4.append(r5);	 Catch:{ all -> 0x005a }
            r5 = r1.toString();	 Catch:{ all -> 0x005a }
            r4.append(r5);	 Catch:{ all -> 0x005a }
            if (r2 == 0) goto L_0x008c;
        L_0x0076:
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
            r5.<init>();	 Catch:{ all -> 0x005a }
            r6 = "\n cause: ";
            r5.append(r6);	 Catch:{ all -> 0x005a }
            r6 = r2.toString();	 Catch:{ all -> 0x005a }
            r5.append(r6);	 Catch:{ all -> 0x005a }
            r5 = r5.toString();	 Catch:{ all -> 0x005a }
            goto L_0x008e;
        L_0x008c:
            r5 = "";
        L_0x008e:
            r4.append(r5);	 Catch:{ all -> 0x005a }
            r4 = r4.toString();	 Catch:{ all -> 0x005a }
            android.util.Log.e(r3, r4);	 Catch:{ all -> 0x005a }
            r0 = -2;
            r1 = r7.mErrorCodeVar;
            if (r1 == 0) goto L_0x009e;
        L_0x009d:
            goto L_0x0053;
        L_0x009e:
            return;
        L_0x009f:
            r2 = r7.mErrorCodeVar;
            if (r2 == 0) goto L_0x00a9;
        L_0x00a3:
            r2 = r7.mErrorCodeVar;
            r3 = (double) r0;
            r2.set(r3);
        L_0x00a9:
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.ActionCommand$MethodCommand.doPerform():void");
        }
    }

    private static class ModeToggleHelper {
        private int mCurModeIndex;
        private int mCurToggleIndex;
        private ArrayList<Integer> mModeIds;
        private ArrayList<String> mModeNames;
        private boolean mToggle;
        private boolean mToggleAll;
        private ArrayList<Integer> mToggleModes;

        private ModeToggleHelper() {
            this.mModeNames = new ArrayList();
            this.mModeIds = new ArrayList();
            this.mToggleModes = new ArrayList();
        }

        /* synthetic */ ModeToggleHelper(AnonymousClass1 x0) {
            this();
        }

        public void addMode(String mode, int id) {
            this.mModeNames.add(mode);
            this.mModeIds.add(Integer.valueOf(id));
        }

        public boolean build(String value) {
            int index = findMode(value);
            if (index >= 0) {
                this.mCurModeIndex = index;
                return true;
            } else if ("toggle".equals(value)) {
                this.mToggleAll = true;
                return true;
            } else {
                String[] modes = value.split(",");
                for (int ind : modes) {
                    int ind2 = findMode(ind2);
                    if (ind2 < 0) {
                        return false;
                    }
                    this.mToggleModes.add(Integer.valueOf(ind2));
                }
                this.mToggle = true;
                return true;
            }
        }

        private int findMode(String name) {
            for (int i = 0; i < this.mModeNames.size(); i++) {
                if (((String) this.mModeNames.get(i)).equals(name)) {
                    return i;
                }
            }
            return -1;
        }

        public void click() {
            int i;
            if (this.mToggle) {
                i = this.mCurToggleIndex + 1;
                this.mCurToggleIndex = i;
                this.mCurToggleIndex = i % this.mToggleModes.size();
                this.mCurModeIndex = ((Integer) this.mToggleModes.get(this.mCurToggleIndex)).intValue();
            } else if (this.mToggleAll) {
                i = this.mCurModeIndex + 1;
                this.mCurModeIndex = i;
                this.mCurModeIndex = i % this.mModeNames.size();
            }
        }

        public String getModeName() {
            return (String) this.mModeNames.get(this.mCurModeIndex);
        }

        public int getModeId() {
            return ((Integer) this.mModeIds.get(this.mCurModeIndex)).intValue();
        }
    }

    protected static class ObjVar {
        private int mIndex;
        private Expression mIndexArr;
        private Variables mVars;

        public ObjVar(String name, Variables vars) {
            this.mVars = vars;
            String rname = name;
            int pos = name.indexOf(91);
            if (pos > 0) {
                try {
                    rname = name.substring(0, pos);
                    this.mIndexArr = Expression.build(vars, name.substring(pos + 1, name.length() - 1));
                } catch (IndexOutOfBoundsException e) {
                }
            }
            this.mIndex = vars.registerVariable(rname);
        }

        public Object get() {
            Object obj = this.mVars.get(this.mIndex);
            if (obj != null) {
                Expression expression = this.mIndexArr;
                if (expression != null && (obj instanceof Object[])) {
                    try {
                        return ((Object[]) obj)[(int) expression.evaluate()];
                    } catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                }
            }
            return obj;
        }
    }

    private static class OnOffCommandHelper {
        protected boolean mIsOn;
        protected boolean mIsToggle;

        public OnOffCommandHelper(String value) {
            if (value.equalsIgnoreCase("toggle")) {
                this.mIsToggle = true;
            } else if (value.equalsIgnoreCase("on")) {
                this.mIsOn = true;
            } else if (value.equalsIgnoreCase("off")) {
                this.mIsOn = false;
            }
        }
    }

    private static class RingModeCommand extends NotificationReceiver {
        private AudioManager mAudioManager;
        private ModeToggleHelper mToggleHelper = new ModeToggleHelper();

        public RingModeCommand(ScreenElement screenElement, String value) {
            super(screenElement, VariableNames.RING_MODE, AudioManager.RINGER_MODE_CHANGED_ACTION);
            this.mToggleHelper.addMode("normal", 2);
            this.mToggleHelper.addMode(AlarmClock.VALUE_RINGTONE_SILENT, 0);
            this.mToggleHelper.addMode("vibrate", 1);
            if (!this.mToggleHelper.build(value)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid ring mode command value: ");
                stringBuilder.append(value);
                Log.e("ActionCommand", stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            if (this.mAudioManager != null) {
                this.mToggleHelper.click();
                int mode = this.mToggleHelper.getModeId();
                this.mAudioManager.setRingerMode(mode);
                updateState(mode);
            }
        }

        /* Access modifiers changed, original: protected */
        public void update() {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) this.mScreenElement.getContext().mContext.getSystemService("audio");
            }
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                updateState(audioManager.getRingerMode());
            }
        }
    }

    private static class SoundCommand extends ActionCommand {
        public static final String TAG_NAME = "SoundCommand";
        private Command mCommand;
        private boolean mKeepCur;
        private boolean mLoop;
        private String mSound;
        private Expression mStreamIdExp;
        private IndexedVariable mStreamIdVar;
        private Expression mVolumeExp;

        public SoundCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mSound = ele.getAttribute("sound");
            this.mKeepCur = Boolean.parseBoolean(ele.getAttribute("keepCur"));
            this.mLoop = Boolean.parseBoolean(ele.getAttribute("loop"));
            this.mCommand = Command.parse(ele.getAttribute("command"));
            this.mVolumeExp = Expression.build(getVariables(), ele.getAttribute("volume"));
            if (this.mVolumeExp == null) {
                Log.e("ActionCommand", "invalid expression in SoundCommand");
            }
            this.mStreamIdExp = Expression.build(getVariables(), ele.getAttribute("streamId"));
            String streamIdVarName = ele.getAttribute("streamIdVar");
            if (!TextUtils.isEmpty(streamIdVarName)) {
                this.mStreamIdVar = new IndexedVariable(streamIdVarName, getVariables(), true);
            }
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            int i = AnonymousClass1.$SwitchMap$miui$maml$SoundManager$Command[this.mCommand.ordinal()];
            if (i == 1) {
                float volume = 0.0f;
                Expression expression = this.mVolumeExp;
                if (expression != null) {
                    volume = (float) expression.evaluate();
                }
                int streamId = getRoot().playSound(this.mSound, new SoundOptions(this.mKeepCur, this.mLoop, volume));
                IndexedVariable indexedVariable = this.mStreamIdVar;
                if (indexedVariable != null) {
                    indexedVariable.set((double) streamId);
                }
            } else if (i == 2 || i == 3 || i == 4) {
                Expression expression2 = this.mStreamIdExp;
                if (expression2 != null) {
                    getRoot().playSound((int) expression2.evaluate(), this.mCommand);
                }
            }
        }
    }

    public static abstract class StateTracker {
        private Boolean mActualState = null;
        private boolean mDeferredStateChangeRequestNeeded = false;
        private boolean mInTransition = false;
        private Boolean mIntendedState = null;

        public abstract int getActualState(Context context);

        public abstract void onActualStateChange(Context context, Intent intent);

        public abstract void requestStateChange(Context context, boolean z);

        public final void toggleState(Context context) {
            int currentState = getTriState(context);
            boolean newState = false;
            if (currentState == 0) {
                newState = true;
            } else if (currentState == 1) {
                newState = false;
            } else if (currentState == 5) {
                Boolean bool = this.mIntendedState;
                if (bool != null) {
                    newState = bool.booleanValue() ^ 1;
                }
            }
            this.mIntendedState = Boolean.valueOf(newState);
            if (this.mInTransition) {
                this.mDeferredStateChangeRequestNeeded = true;
                return;
            }
            this.mInTransition = true;
            requestStateChange(context, newState);
        }

        /* Access modifiers changed, original: protected|final */
        public final void setCurrentState(Context context, int newState) {
            boolean wasInTransition = this.mInTransition;
            if (newState == 0) {
                this.mInTransition = false;
                this.mActualState = Boolean.valueOf(false);
            } else if (newState == 1) {
                this.mInTransition = false;
                this.mActualState = Boolean.valueOf(true);
            } else if (newState == 2) {
                this.mInTransition = true;
                this.mActualState = Boolean.valueOf(false);
            } else if (newState == 3) {
                this.mInTransition = true;
                this.mActualState = Boolean.valueOf(true);
            }
            if (wasInTransition && !this.mInTransition && this.mDeferredStateChangeRequestNeeded) {
                String str = "ActionCommand";
                Log.v(str, "processing deferred state change");
                Boolean bool = this.mActualState;
                if (bool != null) {
                    Boolean bool2 = this.mIntendedState;
                    if (bool2 != null && bool2.equals(bool)) {
                        Log.v(str, "... but intended state matches, so no changes.");
                        this.mDeferredStateChangeRequestNeeded = false;
                    }
                }
                Boolean bool3 = this.mIntendedState;
                if (bool3 != null) {
                    this.mInTransition = true;
                    requestStateChange(context, bool3.booleanValue());
                }
                this.mDeferredStateChangeRequestNeeded = false;
            }
        }

        public final boolean isTurningOn() {
            Boolean bool = this.mIntendedState;
            return bool != null && bool.booleanValue();
        }

        public final int getTriState(Context context) {
            if (this.mInTransition) {
                return 5;
            }
            int actualState = getActualState(context);
            if (actualState == 0) {
                return 0;
            }
            if (actualState != 1) {
                return 5;
            }
            return 1;
        }
    }

    private static class UsbStorageSwitchCommand extends NotificationReceiver {
        private boolean mConnected;
        private OnOffCommandHelper mOnOffHelper;
        private StorageManager mStorageManager;

        public UsbStorageSwitchCommand(ScreenElement screenElement, String value) {
            super(screenElement, "usb_mode", UsbManager.ACTION_USB_STATE);
            this.mOnOffHelper = new OnOffCommandHelper(value);
        }

        public void onNotify(Context context, Intent intent, Object o) {
            if (intent != null) {
                this.mConnected = intent.getExtras().getBoolean("connected");
            }
            super.onNotify(context, intent, o);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            boolean enabled = this.mStorageManager;
            if (enabled) {
                boolean on;
                enabled = enabled.isUsbMassStorageEnabled();
                if (this.mOnOffHelper.mIsToggle) {
                    on = enabled ^ 1;
                } else if (this.mOnOffHelper.mIsOn != enabled) {
                    on = this.mOnOffHelper.mIsOn;
                } else {
                    return;
                }
                updateState(3);
                final boolean _on = on;
                new Thread("StorageSwitchThread") {
                    public void run() {
                        if (_on) {
                            UsbStorageSwitchCommand.this.mStorageManager.enableUsbMassStorage();
                        } else {
                            UsbStorageSwitchCommand.this.mStorageManager.disableUsbMassStorage();
                        }
                        UsbStorageSwitchCommand.this.updateState(_on ? 2 : 1);
                    }
                }.start();
            }
        }

        /* Access modifiers changed, original: protected */
        public void update() {
            if (this.mStorageManager == null) {
                this.mStorageManager = (StorageManager) getContext().getSystemService("storage");
                if (this.mStorageManager == null) {
                    Log.w("ActionCommand", "Failed to get StorageManager");
                    return;
                }
            }
            int i = this.mConnected ? this.mStorageManager.isUsbMassStorageEnabled() ? 2 : 1 : 0;
            updateState(i);
        }
    }

    private static class VariableAssignmentCommand extends ActionCommand {
        public static final String TAG_NAME = "VariableCommand";
        private Expression[] mArrayValues;
        private Expression mExpression;
        private Expression mIndexExpression;
        private String mName;
        private boolean mPersist;
        private boolean mRequestUpdate;
        private VariableType mType;
        private IndexedVariable mVar;

        public VariableAssignmentCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mName = ele.getAttribute("name");
            this.mPersist = Boolean.parseBoolean(ele.getAttribute("persist"));
            this.mRequestUpdate = Boolean.parseBoolean(ele.getAttribute("requestUpdate"));
            this.mType = VariableType.parseType(ele.getAttribute("type"));
            if (TextUtils.isEmpty(this.mName)) {
                Log.e("ActionCommand", "empty name in VariableAssignmentCommand");
            } else {
                this.mVar = new IndexedVariable(this.mName, getVariables(), this.mType.isNumber());
            }
            Variables variables = screenElement.getVariables();
            this.mExpression = Expression.build(variables, ele.getAttribute("expression"));
            if (this.mType.isArray()) {
                this.mIndexExpression = Expression.build(variables, ele.getAttribute("index"));
                this.mArrayValues = Expression.buildMultiple(variables, ele.getAttribute("values"));
            }
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            ScreenElementRoot root = getRoot();
            int i = AnonymousClass1.$SwitchMap$miui$maml$data$VariableType[this.mType.ordinal()];
            Expression expression;
            int i2;
            if (i == 1) {
                expression = this.mExpression;
                if (expression != null) {
                    double v = expression.evaluate();
                    this.mVar.set(v);
                    if (this.mPersist && root.getCapability(2)) {
                        root.saveVar(this.mName, Double.valueOf(v));
                    }
                }
            } else if (i == 2) {
                expression = this.mIndexExpression;
                if (expression == null || this.mExpression == null) {
                    i = this.mArrayValues;
                    if (i != 0) {
                        i = i.length;
                        for (i2 = 0; i2 < i; i2++) {
                            Expression ex = this.mArrayValues[i2];
                            this.mVar.setArr(i2, ex == null ? 0.0d : ex.evaluate());
                        }
                    }
                } else {
                    this.mVar.setArr((int) expression.evaluate(), this.mExpression.evaluate());
                }
            } else if (i != 3) {
                String str = null;
                Expression expression2;
                if (i != 4) {
                    Object obj = null;
                    Expression expression3 = this.mExpression;
                    if (expression3 != null) {
                        str = expression3.evaluateStr();
                    }
                    String name = str;
                    Variables vars = getVariables();
                    if (!TextUtils.isEmpty(name) && vars.existsObj(name)) {
                        obj = vars.get(name);
                    }
                    expression2 = this.mIndexExpression;
                    if (expression2 == null) {
                        this.mVar.set(obj);
                    } else {
                        this.mVar.setArr((int) expression2.evaluate(), obj);
                    }
                } else {
                    expression = this.mIndexExpression;
                    if (expression == null || this.mExpression == null) {
                        i = this.mArrayValues;
                        if (i != 0) {
                            i = i.length;
                            for (i2 = 0; i2 < i; i2++) {
                                expression2 = this.mArrayValues[i2];
                                this.mVar.setArr(i2, expression2 == null ? null : expression2.evaluateStr());
                            }
                        }
                    } else {
                        this.mVar.setArr((int) expression.evaluate(), this.mExpression.evaluateStr());
                    }
                }
            } else {
                String str2 = this.mExpression.evaluateStr();
                this.mVar.set((Object) str2);
                if (this.mPersist && root.getCapability(2)) {
                    root.saveVar(this.mName, str2);
                }
            }
            if (this.mRequestUpdate) {
                root.requestUpdate();
            }
        }
    }

    private static class VariableBinderCommand extends ActionCommand {
        public static final String TAG_NAME = "BinderCommand";
        private VariableBinder mBinder;
        private Command mCommand = Command.Invalid;
        private String mName;

        private enum Command {
            Refresh,
            Invalid
        }

        public VariableBinderCommand(ScreenElement screenElement, Element ele) {
            super(screenElement);
            this.mName = ele.getAttribute("name");
            if (ele.getAttribute("command").equals("refresh")) {
                this.mCommand = Command.Refresh;
            }
        }

        public void init() {
            this.mBinder = getRoot().findBinder(this.mName);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            if (this.mBinder != null && AnonymousClass1.$SwitchMap$miui$maml$ActionCommand$VariableBinderCommand$Command[this.mCommand.ordinal()] == 1) {
                this.mBinder.refresh();
            }
        }
    }

    @Deprecated
    private static class VisibilityProperty extends PropertyCommand {
        public static final String PROPERTY_NAME = "visibility";
        private boolean mIsShow;
        private boolean mIsToggle;

        protected VisibilityProperty(ScreenElement screenElement, Variable targetObj, String value) {
            super(screenElement, targetObj, value);
            if (value.equalsIgnoreCase("toggle")) {
                this.mIsToggle = true;
            } else if (value.equalsIgnoreCase("true")) {
                this.mIsShow = true;
            } else if (value.equalsIgnoreCase("false")) {
                this.mIsShow = false;
            }
        }

        public void doPerform() {
            if (this.mIsToggle) {
                this.mTargetElement.show(this.mTargetElement.isVisible() ^ 1);
            } else {
                this.mTargetElement.show(this.mIsShow);
            }
        }
    }

    public static final class WifiEnableAsyncTask extends AsyncTask<Void, Void, Void> {
        Context mContext;
        boolean mDesiredState;

        public WifiEnableAsyncTask(Context context, boolean desiredState) {
            this.mContext = context;
            this.mDesiredState = desiredState;
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voids) {
            WifiManager wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
            WifiApHelper wifiApHelper = new WifiApHelper(this.mContext);
            if (wifiManager == null) {
                Log.d("ActionCommand", "No wifiManager.");
                return null;
            }
            int wifiApState = wifiManager.getWifiApState();
            if (this.mDesiredState && (wifiApState == 12 || wifiApState == 13)) {
                wifiApHelper.setWifiApEnabled(false);
            }
            wifiManager.setWifiEnabled(this.mDesiredState);
            return null;
        }
    }

    private static final class WifiStateTracker extends StateTracker {
        private static final int MAX_SCAN_ATTEMPT = 3;
        public boolean zConnected;
        private int zScanAttempt;

        private WifiStateTracker() {
            this.zConnected = false;
            this.zScanAttempt = 0;
        }

        /* synthetic */ WifiStateTracker(AnonymousClass1 x0) {
            this();
        }

        public int getActualState(Context context) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                return wifiStateToFiveState(wifiManager.getWifiState());
            }
            return 4;
        }

        /* Access modifiers changed, original: protected */
        public void requestStateChange(Context context, boolean desiredState) {
            new WifiEnableAsyncTask(context, desiredState).execute((Object[]) new Void[0]);
        }

        public void onActualStateChange(Context context, Intent intent) {
            boolean z = false;
            int wifiState;
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                wifiState = intent.getIntExtra("wifi_state", -1);
                setCurrentState(context, wifiStateToFiveState(wifiState));
                if (3 == wifiState) {
                    this.zConnected = true;
                    this.zScanAttempt = 0;
                    return;
                }
                return;
            }
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                wifiState = this.zScanAttempt;
                if (wifiState < 3) {
                    wifiState++;
                    this.zScanAttempt = wifiState;
                    if (wifiState == 3) {
                        this.zConnected = false;
                        return;
                    }
                    return;
                }
                return;
            }
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                this.zScanAttempt = 3;
                DetailedState state = ((NetworkInfo) intent.getParcelableExtra("networkInfo")).getDetailedState();
                if (DetailedState.SCANNING == state || DetailedState.CONNECTING == state || DetailedState.AUTHENTICATING == state || DetailedState.OBTAINING_IPADDR == state || DetailedState.CONNECTED == state) {
                    z = true;
                }
                this.zConnected = z;
            }
        }

        private static int wifiStateToFiveState(int wifiState) {
            if (wifiState == 0) {
                return 3;
            }
            if (wifiState == 1) {
                return 0;
            }
            if (wifiState == 2) {
                return 2;
            }
            if (wifiState != 3) {
                return 4;
            }
            return 1;
        }
    }

    private static class WifiSwitchCommand extends NotificationReceiver {
        private OnOffCommandHelper mOnOffHelper;
        private final StateTracker mWifiState = new WifiStateTracker();

        public WifiSwitchCommand(ScreenElement screenElement, String value) {
            super(screenElement, "wifi_state", NotifierManager.TYPE_WIFI_STATE);
            update();
            this.mOnOffHelper = new OnOffCommandHelper(value);
        }

        public void onNotify(Context context, Intent intent, Object o) {
            this.mWifiState.onActualStateChange(context, intent);
            super.onNotify(context, intent, o);
        }

        /* Access modifiers changed, original: protected */
        public void doPerform() {
            Context context = getContext();
            if (this.mOnOffHelper.mIsToggle) {
                this.mWifiState.toggleState(context);
            } else {
                boolean change = false;
                int triState = this.mWifiState.getTriState(context);
                if (triState != 0) {
                    if (triState == 1 && !this.mOnOffHelper.mIsOn) {
                        change = true;
                    }
                } else if (this.mOnOffHelper.mIsOn) {
                    change = true;
                }
                if (change) {
                    this.mWifiState.requestStateChange(context, this.mOnOffHelper.mIsOn);
                }
            }
            update();
        }

        /* Access modifiers changed, original: protected */
        public void update() {
            int triState = this.mWifiState.getTriState(getContext());
            int i = 0;
            if (triState != 0) {
                int i2 = 1;
                if (triState == 1) {
                    if (!((WifiStateTracker) this.mWifiState).zConnected) {
                        i2 = 2;
                    }
                    updateState(i2);
                    return;
                } else if (triState == 5) {
                    if (this.mWifiState.isTurningOn()) {
                        i = 3;
                    }
                    updateState(i);
                    return;
                } else {
                    return;
                }
            }
            updateState(0);
        }
    }

    public abstract void doPerform();

    public static ActionCommand create(Element ele, ScreenElement screenElement) {
        if (ele == null) {
            return null;
        }
        Expression condition = Expression.build(screenElement.getVariables(), ele.getAttribute(Condition.SCHEME));
        Expression delayCondition = Expression.build(screenElement.getVariables(), ele.getAttribute("delayCondition"));
        long delay = Utils.getAttrAsLong(ele, "delay", 0);
        ActionCommand ret = null;
        String tag = ele.getNodeName();
        if (tag.equals(TAG_NAME)) {
            ret = create(screenElement, ele.getAttribute("target"), ele.getAttribute("value"));
        } else if (tag.equals(VariableAssignmentCommand.TAG_NAME)) {
            ret = new VariableAssignmentCommand(screenElement, ele);
        } else if (tag.equals(VariableBinderCommand.TAG_NAME)) {
            ret = new VariableBinderCommand(screenElement, ele);
        } else if (tag.equals(IntentCommand.TAG_NAME)) {
            ret = new IntentCommand(screenElement, ele);
        } else if (tag.equals(SoundCommand.TAG_NAME)) {
            ret = new SoundCommand(screenElement, ele);
        } else if (tag.equals(ExternCommand.TAG_NAME)) {
            ret = new ExternCommand(screenElement, ele);
        } else if (tag.equals(VibrateCommand.TAG_NAME)) {
            ret = new VibrateCommand(screenElement, ele);
        } else if (tag.equals(MethodCommand.TAG_NAME)) {
            ret = new MethodCommand(screenElement, ele);
        } else if (tag.equals(FieldCommand.TAG_NAME)) {
            ret = new FieldCommand(screenElement, ele);
        } else if (tag.equals(MultiCommand.TAG_NAME) || tag.equals(MultiCommand.TAG_NAME1)) {
            ret = new MultiCommand(screenElement, ele);
        } else if (tag.equals(LoopCommand.TAG_NAME)) {
            ret = new LoopCommand(screenElement, ele);
        } else if (tag.equals(AnimationCommand.TAG_NAME)) {
            ret = new AnimationCommand(screenElement, ele);
        } else {
            String str = "ActionCommand";
            if (tag.equals(str)) {
                ret = new ActionPerformCommand(screenElement, ele);
            } else {
                ActionCommandFactory f = (ActionCommandFactory) screenElement.getContext().getObjectFactory(str);
                if (f != null) {
                    ret = f.create(screenElement, ele);
                }
            }
        }
        if (ret == null) {
            return null;
        }
        if (delayCondition != null) {
            ret = new ConditionCommand(ret, delayCondition);
        }
        if (delay > 0) {
            ret = new DelayCommand(ret, delay);
        }
        if (condition != null) {
            ret = new ConditionCommand(ret, condition);
        }
        return ret;
    }

    protected static ActionCommand create(ScreenElement screenElement, String target, String value) {
        if (TextUtils.isEmpty(target) || TextUtils.isEmpty(value)) {
            return null;
        }
        Variable targetObj = new Variable(target);
        if (targetObj.getObjName() != null) {
            return PropertyCommand.create(screenElement, target, value);
        }
        String property = targetObj.getPropertyName();
        if (COMMAND_RING_MODE.equals(property)) {
            return new RingModeCommand(screenElement, value);
        }
        if (COMMAND_WIFI.equals(property)) {
            return new WifiSwitchCommand(screenElement, value);
        }
        if (COMMAND_DATA.equals(property)) {
            return new DataSwitchCommand(screenElement, value);
        }
        if (COMMAND_BLUETOOTH.equals(property)) {
            return new BluetoothSwitchCommand(screenElement, value);
        }
        if (COMMAND_USB_STORAGE.equals(property)) {
            return new UsbStorageSwitchCommand(screenElement, value);
        }
        return null;
    }

    public ActionCommand(ScreenElement screenElement) {
        this.mScreenElement = screenElement;
    }

    public void perform() {
        doPerform();
    }

    /* Access modifiers changed, original: protected */
    public ScreenElement getScreenElement() {
        return this.mScreenElement;
    }

    /* Access modifiers changed, original: protected|final */
    public final Variables getVariables() {
        return this.mScreenElement.getVariables();
    }

    /* Access modifiers changed, original: protected */
    public ScreenElementRoot getRoot() {
        return this.mScreenElement.getRoot();
    }

    /* Access modifiers changed, original: protected|final */
    public final ScreenContext getScreenContext() {
        return this.mScreenElement.getContext();
    }

    /* Access modifiers changed, original: protected|final */
    public final Context getContext() {
        return getScreenContext().mContext;
    }

    public void init() {
    }

    public void finish() {
    }

    public void pause() {
    }

    public void resume() {
    }
}
