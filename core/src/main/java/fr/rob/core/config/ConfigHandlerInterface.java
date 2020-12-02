package fr.rob.core.config;

import org.codehaus.jackson.node.ObjectNode;

public interface ConfigHandlerInterface {

    String getRootName();
    String getName();
    Object handle(ObjectNode node);

}
