package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import java.util.List;

public class DownloadSubjectStatusComps {

    public BasicPropertyDetails subjectDetails;
    public List<String> statuses;

    public DownloadSubjectStatusComps(BasicPropertyDetails subjectDetails, List<String> statuses) {
        this.subjectDetails = subjectDetails;
        this.statuses = statuses;
    }
}
