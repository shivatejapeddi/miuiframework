package miui.push;

public interface PacketExtension {
    String getElementName();

    String getNamespace();

    String toXML();
}
