package notifierbackend.Job;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

public class Job {
    @Getter
    private final String subscriptionId;
    @Getter
    private final String id;
    @Getter
    private final String className;
    @Getter
    private final String url;
    @Getter
    private final String tag;
    @Getter
    private final String tagValue;

    public Job(String subscriptionId, String id, String className, String url, String tag, String tagValue) {
	this.subscriptionId = subscriptionId;
	this.id = id;
	this.className = className;
	this.url = url;
	this.tag = tag;
	this.tagValue = tagValue;
    }

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
	if (StringUtils.isNotBlank(url)) {
	    stringBuilder.append(url);
	}
	if (StringUtils.isNotBlank(tag)) {
	    stringBuilder.append(tag);
	}
	if (StringUtils.isNotBlank(className)) {
	    stringBuilder.append(className);
	}
	if (StringUtils.isNotBlank(id)) {
	    stringBuilder.append(id);
	}
	if (StringUtils.isNotBlank(tagValue)) {
	    stringBuilder.append(tagValue);
	}
	return stringBuilder.toString();
    }
}
