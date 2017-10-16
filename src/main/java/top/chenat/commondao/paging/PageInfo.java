package top.chenat.commondao.paging;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by ChenAt 2017/10/15.
 * desc:
 */
public class PageInfo<T> {

    private int pageSize;//每页的数量

    private int pageNum;//当前页

    private int size;//当前页的记录数

    private int pageCount;//总页数

    private int  totalCount;//总记录数


    private List<T> datas;


    public PageInfo(List<T> result, int count,int pageNum,int pageSize) {
        this.datas = result;
        this.totalCount = count;
        this.pageNum = pageNum;
        this.size = CollectionUtils.isEmpty(result) ? 0 : result.size();
        this.pageSize = pageSize;
        this.pageCount = totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1);
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", size=" + size +
                ", pageCount=" + pageCount +
                ", totalCount=" + totalCount +
                ", datas=" + datas +
                '}';
    }
}
