package crawler.taobaoke;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by li3-mac on 2017/11/23.
 */
public class DataPipeline implements Pipeline {

    private String data;

    @Override
    public void process(ResultItems resultItems, Task task) {
        data = resultItems.getAll().get("data").toString();
    }

    public String getData() {
        return data;
    }
}
