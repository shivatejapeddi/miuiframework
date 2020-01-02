package android.sax;

class Children {
    Child[] children = new Child[16];

    static class Child extends Element {
        final int hash;
        Child next;

        Child(Element parent, String uri, String localName, int depth, int hash) {
            super(parent, uri, localName, depth);
            this.hash = hash;
        }
    }

    Children() {
    }

    /* Access modifiers changed, original: 0000 */
    public Element getOrCreate(Element parent, String uri, String localName) {
        int hash = (uri.hashCode() * 31) + localName.hashCode();
        int index = hash & 15;
        Child current = this.children[index];
        Child current2;
        if (current == null) {
            current2 = new Child(parent, uri, localName, parent.depth + 1, hash);
            this.children[index] = current2;
            return current2;
        }
        while (true) {
            if (current.hash == hash && current.uri.compareTo(uri) == 0 && current.localName.compareTo(localName) == 0) {
                return current;
            }
            Child previous = current;
            current = current.next;
            if (current == null) {
                current2 = new Child(parent, uri, localName, parent.depth + 1, hash);
                previous.next = current2;
                return current2;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Element get(String uri, String localName) {
        int hash = (uri.hashCode() * 31) + localName.hashCode();
        Child current = this.children[hash & 15];
        if (current == null) {
            return null;
        }
        while (true) {
            if (current.hash == hash && current.uri.compareTo(uri) == 0 && current.localName.compareTo(localName) == 0) {
                return current;
            }
            current = current.next;
            if (current == null) {
                return null;
            }
        }
    }
}
