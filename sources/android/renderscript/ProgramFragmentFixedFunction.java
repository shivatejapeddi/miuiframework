package android.renderscript;

import android.annotation.UnsupportedAppUsage;
import android.renderscript.Program.BaseProgramBuilder;
import android.renderscript.Program.TextureType;

public class ProgramFragmentFixedFunction extends ProgramFragment {

    /* renamed from: android.renderscript.ProgramFragmentFixedFunction$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$EnvMode = new int[EnvMode.values().length];
        static final /* synthetic */ int[] $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format = new int[Format.values().length];

        static {
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$EnvMode[EnvMode.REPLACE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$EnvMode[EnvMode.MODULATE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$EnvMode[EnvMode.DECAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[Format.ALPHA.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[Format.LUMINANCE_ALPHA.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[Format.RGB.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[Format.RGBA.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public static class Builder {
        public static final int MAX_TEXTURE = 2;
        int mNumTextures;
        boolean mPointSpriteEnable = false;
        RenderScript mRS;
        String mShader;
        Slot[] mSlots = new Slot[2];
        boolean mVaryingColorEnable;

        public enum EnvMode {
            REPLACE(1),
            MODULATE(2),
            DECAL(3);
            
            int mID;

            private EnvMode(int id) {
                this.mID = id;
            }
        }

        public enum Format {
            ALPHA(1),
            LUMINANCE_ALPHA(2),
            RGB(3),
            RGBA(4);
            
            int mID;

            private Format(int id) {
                this.mID = id;
            }
        }

        private class Slot {
            EnvMode env;
            Format format;

            Slot(EnvMode _env, Format _fmt) {
                this.env = _env;
                this.format = _fmt;
            }
        }

        private void buildShaderString() {
            this.mShader = "//rs_shader_internal\n";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mShader);
            stringBuilder.append("varying lowp vec4 varColor;\n");
            this.mShader = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mShader);
            stringBuilder.append("varying vec2 varTex0;\n");
            this.mShader = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mShader);
            stringBuilder.append("void main() {\n");
            this.mShader = stringBuilder.toString();
            if (this.mVaryingColorEnable) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mShader);
                stringBuilder.append("  lowp vec4 col = varColor;\n");
                this.mShader = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.mShader);
                stringBuilder.append("  lowp vec4 col = UNI_Color;\n");
                this.mShader = stringBuilder.toString();
            }
            if (this.mNumTextures != 0) {
                if (this.mPointSpriteEnable) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mShader);
                    stringBuilder.append("  vec2 t0 = gl_PointCoord;\n");
                    this.mShader = stringBuilder.toString();
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.mShader);
                    stringBuilder.append("  vec2 t0 = varTex0.xy;\n");
                    this.mShader = stringBuilder.toString();
                }
            }
            for (int i = 0; i < this.mNumTextures; i++) {
                int i2 = AnonymousClass1.$SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$EnvMode[this.mSlots[i].env.ordinal()];
                String str;
                StringBuilder stringBuilder2;
                if (i2 == 1) {
                    i2 = AnonymousClass1.$SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[this.mSlots[i].format.ordinal()];
                    if (i2 != 1) {
                        str = "  col.rgba = texture2D(UNI_Tex0, t0).rgba;\n";
                        if (i2 == 2) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append(str);
                            this.mShader = stringBuilder2.toString();
                        } else if (i2 == 3) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append("  col.rgb = texture2D(UNI_Tex0, t0).rgb;\n");
                            this.mShader = stringBuilder2.toString();
                        } else if (i2 == 4) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append(str);
                            this.mShader = stringBuilder2.toString();
                        }
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(this.mShader);
                        stringBuilder2.append("  col.a = texture2D(UNI_Tex0, t0).a;\n");
                        this.mShader = stringBuilder2.toString();
                    }
                } else if (i2 == 2) {
                    i2 = AnonymousClass1.$SwitchMap$android$renderscript$ProgramFragmentFixedFunction$Builder$Format[this.mSlots[i].format.ordinal()];
                    if (i2 != 1) {
                        str = "  col.rgba *= texture2D(UNI_Tex0, t0).rgba;\n";
                        if (i2 == 2) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append(str);
                            this.mShader = stringBuilder2.toString();
                        } else if (i2 == 3) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append("  col.rgb *= texture2D(UNI_Tex0, t0).rgb;\n");
                            this.mShader = stringBuilder2.toString();
                        } else if (i2 == 4) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.mShader);
                            stringBuilder2.append(str);
                            this.mShader = stringBuilder2.toString();
                        }
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(this.mShader);
                        stringBuilder2.append("  col.a *= texture2D(UNI_Tex0, t0).a;\n");
                        this.mShader = stringBuilder2.toString();
                    }
                } else if (i2 == 3) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.mShader);
                    stringBuilder2.append("  col = texture2D(UNI_Tex0, t0);\n");
                    this.mShader = stringBuilder2.toString();
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mShader);
            stringBuilder.append("  gl_FragColor = col;\n");
            this.mShader = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mShader);
            stringBuilder.append("}\n");
            this.mShader = stringBuilder.toString();
        }

        @UnsupportedAppUsage
        public Builder(RenderScript rs) {
            this.mRS = rs;
        }

        @UnsupportedAppUsage
        public Builder setTexture(EnvMode env, Format fmt, int slot) throws IllegalArgumentException {
            if (slot < 0 || slot >= 2) {
                throw new IllegalArgumentException("MAX_TEXTURE exceeded.");
            }
            this.mSlots[slot] = new Slot(env, fmt);
            return this;
        }

        public Builder setPointSpriteTexCoordinateReplacement(boolean enable) {
            this.mPointSpriteEnable = enable;
            return this;
        }

        @UnsupportedAppUsage
        public Builder setVaryingColor(boolean enable) {
            this.mVaryingColorEnable = enable;
            return this;
        }

        @UnsupportedAppUsage
        public ProgramFragmentFixedFunction create() {
            InternalBuilder sb = new InternalBuilder(this.mRS);
            this.mNumTextures = 0;
            for (int i = 0; i < 2; i++) {
                if (this.mSlots[i] != null) {
                    this.mNumTextures++;
                }
            }
            buildShaderString();
            sb.setShader(this.mShader);
            Type constType = null;
            if (!this.mVaryingColorEnable) {
                android.renderscript.Element.Builder b = new android.renderscript.Element.Builder(this.mRS);
                b.add(Element.F32_4(this.mRS), "Color");
                android.renderscript.Type.Builder typeBuilder = new android.renderscript.Type.Builder(this.mRS, b.create());
                typeBuilder.setX(1);
                constType = typeBuilder.create();
                sb.addConstant(constType);
            }
            for (int i2 = 0; i2 < this.mNumTextures; i2++) {
                sb.addTexture(TextureType.TEXTURE_2D);
            }
            ProgramFragmentFixedFunction pf = sb.create();
            pf.mTextureCount = 2;
            if (!this.mVaryingColorEnable) {
                Allocation constantData = Allocation.createTyped(this.mRS, constType);
                FieldPacker fp = new FieldPacker(16);
                fp.addF32(new Float4(1.0f, 1.0f, 1.0f, 1.0f));
                constantData.setFromFieldPacker(0, fp);
                pf.bindConstants(constantData, 0);
            }
            return pf;
        }
    }

    static class InternalBuilder extends BaseProgramBuilder {
        public InternalBuilder(RenderScript rs) {
            super(rs);
        }

        public ProgramFragmentFixedFunction create() {
            int i;
            int i2;
            this.mRS.validate();
            long[] tmp = new long[((((this.mInputCount + this.mOutputCount) + this.mConstantCount) + this.mTextureCount) * 2)];
            String[] texNames = new String[this.mTextureCount];
            int idx = 0;
            for (i = 0; i < this.mInputCount; i++) {
                i2 = idx + 1;
                tmp[idx] = (long) ProgramParam.INPUT.mID;
                idx = i2 + 1;
                tmp[i2] = this.mInputs[i].getID(this.mRS);
            }
            for (i = 0; i < this.mOutputCount; i++) {
                i2 = idx + 1;
                tmp[idx] = (long) ProgramParam.OUTPUT.mID;
                idx = i2 + 1;
                tmp[i2] = this.mOutputs[i].getID(this.mRS);
            }
            for (i = 0; i < this.mConstantCount; i++) {
                i2 = idx + 1;
                tmp[idx] = (long) ProgramParam.CONSTANT.mID;
                idx = i2 + 1;
                tmp[i2] = this.mConstants[i].getID(this.mRS);
            }
            for (i = 0; i < this.mTextureCount; i++) {
                i2 = idx + 1;
                tmp[idx] = (long) ProgramParam.TEXTURE_TYPE.mID;
                idx = i2 + 1;
                tmp[i2] = (long) this.mTextureTypes[i].mID;
                texNames[i] = this.mTextureNames[i];
            }
            ProgramFragmentFixedFunction pf = new ProgramFragmentFixedFunction(this.mRS.nProgramFragmentCreate(this.mShader, texNames, tmp), this.mRS);
            initProgram(pf);
            return pf;
        }
    }

    ProgramFragmentFixedFunction(long id, RenderScript rs) {
        super(id, rs);
    }
}
