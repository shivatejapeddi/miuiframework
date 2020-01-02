package android.content.pm;

import android.content.pm.PackageParser.Package;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.Collection;

@VisibleForTesting
public abstract class PackageSharedLibraryUpdater {
    public abstract void updatePackage(Package packageR);

    static void removeLibrary(Package pkg, String libraryName) {
        pkg.usesLibraries = ArrayUtils.remove(pkg.usesLibraries, (Object) libraryName);
        pkg.usesOptionalLibraries = ArrayUtils.remove(pkg.usesOptionalLibraries, (Object) libraryName);
    }

    static <T> ArrayList<T> prefix(ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new ArrayList();
        }
        cur.add(0, val);
        return cur;
    }

    private static boolean isLibraryPresent(ArrayList<String> usesLibraries, ArrayList<String> usesOptionalLibraries, String apacheHttpLegacy) {
        return ArrayUtils.contains((Collection) usesLibraries, (Object) apacheHttpLegacy) || ArrayUtils.contains((Collection) usesOptionalLibraries, (Object) apacheHttpLegacy);
    }

    /* Access modifiers changed, original: 0000 */
    public void prefixImplicitDependency(Package pkg, String existingLibrary, String implicitDependency) {
        Collection usesLibraries = pkg.usesLibraries;
        Collection usesOptionalLibraries = pkg.usesOptionalLibraries;
        if (!isLibraryPresent(usesLibraries, usesOptionalLibraries, implicitDependency)) {
            if (ArrayUtils.contains(usesLibraries, (Object) existingLibrary)) {
                prefix(usesLibraries, implicitDependency);
            } else if (ArrayUtils.contains(usesOptionalLibraries, (Object) existingLibrary)) {
                prefix(usesOptionalLibraries, implicitDependency);
            }
            pkg.usesLibraries = usesLibraries;
            pkg.usesOptionalLibraries = usesOptionalLibraries;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void prefixRequiredLibrary(Package pkg, String libraryName) {
        ArrayList<String> usesLibraries = pkg.usesLibraries;
        if (!isLibraryPresent(usesLibraries, pkg.usesOptionalLibraries, libraryName)) {
            pkg.usesLibraries = prefix(usesLibraries, libraryName);
        }
    }
}
