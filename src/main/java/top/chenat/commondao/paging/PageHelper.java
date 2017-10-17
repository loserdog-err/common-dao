package top.chenat.commondao.paging;

import java.util.Collection;

/**
 * Created by ChenAt 2017/10/16.
 * desc:
 */
public class PageHelper {

    protected static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

    /**
     * 设置 Page 参数
     *
     * @param page
     */
    protected static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    /**
     * 获取 Page 参数
     *
     * @return
     */
    public static <T> Page<T> getLocalPage() {
        return LOCAL_PAGE.get();
    }

    /**
     * 移除本地变量
     */
    public static void clearPage() {
        LOCAL_PAGE.remove();
    }


    /**
     * 开始分页
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     */
    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        return startPage(pageNum, pageSize, true);
    }

    /**
     * 开始分页
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @param count    是否进行count查询
     */
    public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count) {
        Page<E> page = new Page<>(pageNum, pageSize, count);
        setLocalPage(page);
        return page;
    }


    public static Object afterPage(Object result) {
        Page page = getLocalPage();
        if (page == null) {
            return result;
        }
        page.addAll((Collection) result);
        if (!page.isCount()) {
            page.setTotalCount(-1);
        }
        return page;
    }
}
