package net.ravendb.client.documents.operations;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.extensions.JsonExtensions;
import net.ravendb.client.http.RavenCommand;
import net.ravendb.client.http.ServerNode;
import net.ravendb.client.primitives.Reference;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

public class GetOperationStateOperation implements IMaintenanceOperation<ObjectNode> {

    private final long _id;

    public GetOperationStateOperation(long id) {
        _id = id;
    }

    @Override
    public RavenCommand<ObjectNode> getCommand(DocumentConventions conventions) {
        return new GetOperationStateCommand(DocumentConventions.defaultConventions, _id);
    }

    public static class GetOperationStateCommand extends RavenCommand<ObjectNode> {
        @Override
        public boolean isReadRequest() {
            return true;
        }

        private final DocumentConventions _conventions;
        private final long _id;

        public GetOperationStateCommand(DocumentConventions conventions, long id) {
            super(ObjectNode.class);
            _conventions = conventions;
            _id = id;
        }

        @Override
        public HttpRequestBase createRequest(ServerNode node, Reference<String> url) {
            url.value = node.getUrl() + "/databases/" + node.getDatabase() + "/operations/state?id=" + _id;


            return new HttpGet();
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        @Override
        public void setResponse(String response, boolean fromCache) throws IOException {
            if (response == null) {
                return;
            }

            ObjectNode node = (ObjectNode) JsonExtensions.getDefaultMapper().readTree(response);
            result = node;
        }
    }
}
