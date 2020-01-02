package android.gesture;

import java.util.ArrayList;

abstract class Learner {
    private final ArrayList<Instance> mInstances = new ArrayList();

    public abstract ArrayList<Prediction> classify(int i, int i2, float[] fArr);

    Learner() {
    }

    /* Access modifiers changed, original: 0000 */
    public void addInstance(Instance instance) {
        this.mInstances.add(instance);
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<Instance> getInstances() {
        return this.mInstances;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeInstance(long id) {
        ArrayList<Instance> instances = this.mInstances;
        int count = instances.size();
        for (int i = 0; i < count; i++) {
            Instance instance = (Instance) instances.get(i);
            if (id == instance.id) {
                instances.remove(instance);
                return;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeInstances(String name) {
        ArrayList<Instance> toDelete = new ArrayList();
        ArrayList<Instance> instances = this.mInstances;
        int count = instances.size();
        for (int i = 0; i < count; i++) {
            Instance instance = (Instance) instances.get(i);
            if ((instance.label == null && name == null) || (instance.label != null && instance.label.equals(name))) {
                toDelete.add(instance);
            }
        }
        instances.removeAll(toDelete);
    }
}
