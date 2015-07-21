package anb.ground.protocols;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "protocolName" })
public interface DefaultRequest {
	public String getProtocolName();
}
