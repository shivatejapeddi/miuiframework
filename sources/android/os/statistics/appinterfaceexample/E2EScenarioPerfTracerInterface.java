package android.os.statistics.appinterfaceexample;

import android.os.Bundle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

class E2EScenarioPerfTracerInterface {
    public static final int STATISTICS_MODE_AVERAGE = 1;
    public static final int STATISTICS_MODE_DISTRIBUTION = 4;
    public static final int STATISTICS_MODE_HISTORY = 2;
    public static final int STATISTICS_MODE_NONE = 0;
    private static volatile Method sAbortMatchingScenarioWithTag;
    private static volatile Method sAbortSpecificScenario;
    private static volatile Method sAsyncBeginScenarioWithTagAndPayload;
    private static volatile Method sBeginScenarioWithTagAndPayload;
    private static volatile Class<?> sE2EScenarioClass;
    private static volatile Constructor<?> sE2EScenarioConstructor;
    private static volatile Class<?> sE2EScenarioPayloadClass;
    private static volatile Constructor<?> sE2EScenarioPayloadConstuctor;
    private static volatile Method sE2EScenarioPayloadMethodPutAll;
    private static volatile Method sE2EScenarioPayloadMethodPutValues;
    private static volatile Class<?> sE2EScenarioPerfTracerClass;
    private static volatile Class<?> sE2EScenarioSettingsClass;
    private static volatile Constructor<?> sE2EScenarioSettingsConstructor;
    private static volatile Method sE2EScenarioSettingsMethodEnableAutoAnalysis;
    private static volatile Method sE2EScenarioSettingsMethodSetHistoryLimitPerDay;
    private static volatile Method sE2EScenarioSettingsMethodSetStatisticsMode;
    private static volatile Method sFinishMatchingScenarioWithTagAndPayload;
    private static volatile Method sFinishSpecificScenarioWithPayload;
    private static final Object sInitLockObject = new Object();
    private static volatile boolean sInitialized;

    public static class E2EScenario {
        private final Object scenario;

        private E2EScenario(Object scenario) {
            this.scenario = scenario;
        }
    }

    public static class E2EScenarioPayload {
        private final Object payload;

        private E2EScenarioPayload(Object payload) {
            this.payload = payload;
        }

        public void putValues(Object... keyandvalues) {
            if (E2EScenarioPerfTracerInterface.sE2EScenarioPayloadMethodPutValues != null) {
                try {
                    E2EScenarioPerfTracerInterface.sE2EScenarioPayloadMethodPutValues.invoke(this.payload, new Object[]{keyandvalues});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void putAll(Map map) {
            if (E2EScenarioPerfTracerInterface.sE2EScenarioPayloadMethodPutAll != null) {
                try {
                    E2EScenarioPerfTracerInterface.sE2EScenarioPayloadMethodPutAll.invoke(this.payload, new Object[]{map});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static class E2EScenarioSettings {
        private final Object scenarioSettings;

        private E2EScenarioSettings(Object scenarioSettings) {
            this.scenarioSettings = scenarioSettings;
        }

        public void setStatisticsMode(int mode) {
            if (E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodSetStatisticsMode != null) {
                try {
                    E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodSetStatisticsMode.invoke(this.scenarioSettings, new Object[]{Integer.valueOf(mode)});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void setHistoryLimitPerDay(int historyLimitPerDay) {
            if (E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodSetHistoryLimitPerDay != null) {
                try {
                    E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodSetHistoryLimitPerDay.invoke(this.scenarioSettings, new Object[]{Integer.valueOf(historyLimitPerDay)});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void enableAutoAnalysis(int slownessFloorThresholdMillis, int timeoutMillisOfAutoAnalysis) {
            if (E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodEnableAutoAnalysis != null) {
                try {
                    E2EScenarioPerfTracerInterface.sE2EScenarioSettingsMethodEnableAutoAnalysis.invoke(this.scenarioSettings, new Object[]{Integer.valueOf(slownessFloorThresholdMillis), Integer.valueOf(timeoutMillisOfAutoAnalysis)});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private E2EScenarioPerfTracerInterface() {
    }

    public static void init() {
        if (!sInitialized) {
            synchronized (sInitLockObject) {
                if (!sInitialized) {
                    try {
                        sE2EScenarioClass = Class.forName("android.os.statistics.E2EScenario");
                        sE2EScenarioConstructor = sE2EScenarioClass.getConstructor(new Class[]{String.class, String.class, String.class});
                    } catch (Exception e) {
                    }
                    try {
                        sE2EScenarioSettingsClass = Class.forName("android.os.statistics.E2EScenarioSettings");
                        sE2EScenarioSettingsConstructor = sE2EScenarioSettingsClass.getConstructor(new Class[0]);
                        sE2EScenarioSettingsMethodSetStatisticsMode = sE2EScenarioSettingsClass.getMethod("setStatisticsMode", new Class[]{Integer.TYPE});
                        sE2EScenarioSettingsMethodSetHistoryLimitPerDay = sE2EScenarioSettingsClass.getMethod("setHistoryLimitPerDay", new Class[]{Integer.TYPE});
                        sE2EScenarioSettingsMethodEnableAutoAnalysis = sE2EScenarioSettingsClass.getMethod("enableAutoAnalysis", new Class[]{Integer.TYPE, Integer.TYPE});
                    } catch (Exception e2) {
                    }
                    try {
                        sE2EScenarioPayloadClass = Class.forName("android.os.statistics.E2EScenarioPayload");
                        sE2EScenarioPayloadConstuctor = sE2EScenarioPayloadClass.getConstructor(new Class[0]);
                        sE2EScenarioPayloadMethodPutValues = sE2EScenarioPayloadClass.getMethod("putValues", new Class[]{Object[].class});
                        sE2EScenarioPayloadMethodPutAll = sE2EScenarioPayloadClass.getMethod("putAll", new Class[]{Map.class});
                    } catch (Exception e3) {
                    }
                    try {
                        sE2EScenarioPerfTracerClass = Class.forName("android.os.statistics.E2EScenarioPerfTracer");
                        sBeginScenarioWithTagAndPayload = sE2EScenarioPerfTracerClass.getMethod("beginScenario", new Class[]{sE2EScenarioClass, sE2EScenarioSettingsClass, String.class, sE2EScenarioPayloadClass});
                        try {
                            sAsyncBeginScenarioWithTagAndPayload = sE2EScenarioPerfTracerClass.getMethod("asyncBeginScenario", new Class[]{sE2EScenarioClass, sE2EScenarioSettingsClass, String.class, sE2EScenarioPayloadClass});
                        } catch (Exception e4) {
                        }
                        sAbortMatchingScenarioWithTag = sE2EScenarioPerfTracerClass.getMethod("abortScenario", new Class[]{sE2EScenarioClass, String.class});
                        sAbortSpecificScenario = sE2EScenarioPerfTracerClass.getMethod("abortScenario", new Class[]{Bundle.class});
                        sFinishMatchingScenarioWithTagAndPayload = sE2EScenarioPerfTracerClass.getMethod("finishScenario", new Class[]{sE2EScenarioClass, String.class, sE2EScenarioPayloadClass});
                        sFinishSpecificScenarioWithPayload = sE2EScenarioPerfTracerClass.getMethod("finishScenario", new Class[]{Bundle.class, sE2EScenarioPayloadClass});
                    } catch (Exception e5) {
                    }
                    sInitialized = true;
                }
            }
        }
    }

    public static boolean isUsable() {
        if (!sInitialized) {
            init();
        }
        return sE2EScenarioClass != null;
    }

    public static E2EScenario createE2EScenario(String namespace, String category, String name) {
        if (!sInitialized) {
            init();
        }
        Object result = null;
        if (sE2EScenarioConstructor != null) {
            try {
                result = sE2EScenarioConstructor.newInstance(new Object[]{namespace, category, name});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result == null ? null : new E2EScenario(result);
    }

    public static E2EScenarioSettings createE2EScenarioSettings() {
        if (!sInitialized) {
            init();
        }
        Object result = null;
        if (sE2EScenarioSettingsConstructor != null) {
            try {
                result = sE2EScenarioSettingsConstructor.newInstance(new Object[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result == null ? null : new E2EScenarioSettings(result);
    }

    public static E2EScenarioPayload createE2EScenarioPayload() {
        if (!sInitialized) {
            init();
        }
        Object result = null;
        if (sE2EScenarioPayloadConstuctor != null) {
            try {
                result = sE2EScenarioPayloadConstuctor.newInstance(new Object[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result == null ? null : new E2EScenarioPayload(result);
    }

    public static Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings) {
        return beginScenario(scenario, scenarioSettings, "", null);
    }

    public static Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, String tag) {
        return beginScenario(scenario, scenarioSettings, tag, null);
    }

    public static Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, E2EScenarioPayload payload) {
        return beginScenario(scenario, scenarioSettings, "", payload);
    }

    public static Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, String tag, E2EScenarioPayload payload) {
        if (scenario == null || scenarioSettings == null) {
            return null;
        }
        if (!sInitialized) {
            init();
        }
        if (sBeginScenarioWithTagAndPayload != null) {
            try {
                Method method = sBeginScenarioWithTagAndPayload;
                Object[] objArr = new Object[4];
                objArr[0] = scenario.scenario;
                objArr[1] = scenarioSettings.scenarioSettings;
                objArr[2] = tag;
                objArr[3] = payload == null ? null : payload.payload;
                return (Bundle) method.invoke(null, objArr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static void asyncBeginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings) {
        asyncBeginScenario(scenario, scenarioSettings, "", null);
    }

    public static void asyncBeginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, String tag) {
        asyncBeginScenario(scenario, scenarioSettings, tag, null);
    }

    public static void asyncBeginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, E2EScenarioPayload payload) {
        asyncBeginScenario(scenario, scenarioSettings, "", payload);
    }

    public static void asyncBeginScenario(E2EScenario scenario, E2EScenarioSettings scenarioSettings, String tag, E2EScenarioPayload payload) {
        if (scenario != null && scenarioSettings != null) {
            if (!sInitialized) {
                init();
            }
            Method method;
            Object[] objArr;
            if (sAsyncBeginScenarioWithTagAndPayload != null) {
                try {
                    method = sAsyncBeginScenarioWithTagAndPayload;
                    objArr = new Object[4];
                    objArr[0] = scenario.scenario;
                    objArr[1] = scenarioSettings.scenarioSettings;
                    objArr[2] = tag;
                    objArr[3] = payload == null ? null : payload.payload;
                    method.invoke(null, objArr);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (sBeginScenarioWithTagAndPayload != null) {
                try {
                    method = sBeginScenarioWithTagAndPayload;
                    objArr = new Object[4];
                    objArr[0] = scenario.scenario;
                    objArr[1] = scenarioSettings.scenarioSettings;
                    objArr[2] = tag;
                    objArr[3] = payload == null ? null : payload.payload;
                    method.invoke(null, objArr);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    }

    public static void abortScenario(E2EScenario scenario) {
        abortScenario(scenario, "");
    }

    public static void abortScenario(E2EScenario scenario, String tag) {
        if (scenario != null) {
            if (!sInitialized) {
                init();
            }
            if (sAbortMatchingScenarioWithTag != null) {
                try {
                    sAbortMatchingScenarioWithTag.invoke(null, new Object[]{scenario.scenario, tag});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void abortScenario(Bundle scenarioBundle) {
        if (scenarioBundle != null) {
            if (!sInitialized) {
                init();
            }
            if (sAbortSpecificScenario != null) {
                try {
                    sAbortSpecificScenario.invoke(null, new Object[]{scenarioBundle});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void finishScenario(E2EScenario scenario) {
        finishScenario(scenario, "", null);
    }

    public static void finishScenario(E2EScenario scenario, E2EScenarioPayload payload) {
        finishScenario(scenario, "", payload);
    }

    public static void finishScenario(E2EScenario scenario, String tag) {
        finishScenario(scenario, tag, null);
    }

    public static void finishScenario(E2EScenario scenario, String tag, E2EScenarioPayload payload) {
        if (scenario != null) {
            if (!sInitialized) {
                init();
            }
            if (sFinishMatchingScenarioWithTagAndPayload != null) {
                try {
                    Method method = sFinishMatchingScenarioWithTagAndPayload;
                    Object[] objArr = new Object[3];
                    objArr[0] = scenario.scenario;
                    objArr[1] = tag;
                    objArr[2] = payload == null ? null : payload.payload;
                    method.invoke(null, objArr);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void finishScenario(Bundle scenarioBundle) {
        finishScenario(scenarioBundle, null);
    }

    public static void finishScenario(Bundle scenarioBundle, E2EScenarioPayload payload) {
        if (scenarioBundle != null) {
            if (!sInitialized) {
                init();
            }
            if (sFinishSpecificScenarioWithPayload != null) {
                try {
                    Method method = sFinishSpecificScenarioWithPayload;
                    Object[] objArr = new Object[2];
                    objArr[0] = scenarioBundle;
                    objArr[1] = payload == null ? null : payload.payload;
                    method.invoke(null, objArr);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
