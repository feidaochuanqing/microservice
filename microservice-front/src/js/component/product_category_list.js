import React from 'react';
import ReactDOM from 'react-dom';
import { Table, Button, Icon, Popconfirm, message } from 'antd';
import { Input } from 'antd';
import reqwest from 'reqwest';
import ProductCategoryComponent from './product_category.js'
import PubSub from 'pubsub-js';

const Search = Input.Search;

const deleteCategoryEvent = "deleteCategory";
const updateCategoryEvent = "updateCategory";

//产品类别组件
export default class ProductCategoryListComponent extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      pagination: {},
      loading: false,
      categoryName: '',
      editCategory: {},
      isShowProduct: false
    }
  }

  //删除之后进行提示并重新加载数据
  onDelete(e, key) {
    var myFetchOptions = { method: 'GET' };
    const returnjson = {};
    let url = "http://localhost:8011/productCategory/delete?categoryId=" + key;
    fetch(url, myFetchOptions)
      .then(response => response.json())
      .then(json => {
        console.log("json->" + json);
        if (json.isOk) {
          message.success(json.message);
          this.fetch();
        } else {
          message.error(json.message);
        }
      });

  }

  //显示更新窗体
  onUpdate(e, product) {
    this.setState({ editCategory: product, isShowProduct: true });
  }

  //分页
  handleTableChange(pagination, filters, sorter) {
    const pager = this.state.pagination;
    pager.current = pagination.current;
    this.setState({
      pagination: pager
    });

    const params = {
      pageSize: pagination.pageSize,
      pageNumber: pagination.current,
      productName: this.state.productName
    };
    this.fetch(params);
  }

  //创建框
  handleShowCategory(e) {
    let editCategory = { id: '', categoryCode: '', categoryName: '' };
    this.setState({ editCategory: editCategory, isShowProduct: true });
  }

  //加载数据
  fetch(params = {}) {
    console.log('params:', params);
    this.setState({ loading: true });
    reqwest({
      url: 'http://localhost:8011/productCategory/listByPage',
      method: 'get',
      data: params,
      type: 'json',
    }).then((data) => {
      const pagination = this.state.pagination;
      pagination.total = data.totalCount;
      pagination.pageSize = data.numPerPage;
      pagination.current = data.currentPage;
      this.setState({
        loading: false,
        data: data.recordList,
        pagination: pagination
      });
    });
  }

  //订阅事件
  componentDidMount() {
    PubSub.unsubscribe(deleteCategoryEvent);
    PubSub.unsubscribe(updateCategoryEvent);
    PubSub.subscribe(deleteCategoryEvent, this.onDelete.bind(this));
    PubSub.subscribe(updateCategoryEvent, this.onUpdate.bind(this));
    this.fetch();
  }

  //产品窗口关闭之后的事件处理
  afterClosed(value) {
    this.setState({
      isShowProduct: false
    });
    this.fetch();
  }

  //搜索
  serchClick(value) {
    console.log("pagination:" + this.state.pagination);
    const pager = this.state.pagination;
    const params = {
      pageSize: pager.pageSize,
      pageNumber: 1,
      categoryName: value
    };
    this.setState({
      categoryName: value
    });
    this.fetch(params);
  }

  render() {
    let columns = [{
      title: '产品类别编号',
      dataIndex: 'categoryCode',
      width: '20%',
      key: 'categoryCode'
    }, {
      title: '产品类别名称',
      dataIndex: 'categoryName',
      width: '20%',
      key: 'categoryName'
    }, {
      title: '操作',
      key: 'id',
      render(text, record) {
        return (
          <span>
            <Popconfirm title="确定删除么？?"
              onConfirm={() => {
                PubSub.publish(deleteCategoryEvent, record.id);
              }}>
              <a href="#">删除</a>
            </Popconfirm>
            &nbsp;&nbsp;&nbsp;&nbsp;
              <a href="#" onClick={() => {
              PubSub.publish(updateCategoryEvent, record);
            }} >修改</a>
          </span>
        );
      }
    }];
    console.log("this.state.editCategory id->" + this.state.editCategory.id);

    let showProductCategoryComp = this.state.isShowProduct ? <ProductCategoryComponent
      categoryId={this.state.editCategory.id}
      categoryCode={this.state.editCategory.categoryCode}
      categoryName={this.state.editCategory.categoryName}
      afterClosed={this.afterClosed.bind(this)}
    /> : null;
    return (
      <div>
        {showProductCategoryComp}
        <div style={{ marginBottom: 16 }}>
          <Search
            placeholder="产品类别名称"
            style={{ width: 200 }}
            onSearch={this.serchClick.bind(this)}
          />
          <Button style={{ float: `right` }}
            size="large" type="ghost" onClick={this.handleShowCategory.bind(this)}
          >
            创建
          </Button>
        </div>

        <Table columns={columns}
          bordered
          size="small"
          rowKey={record => record.id}
          dataSource={this.state.data}
          pagination={this.state.pagination}
          loading={this.state.loading}
          onChange={this.handleTableChange.bind(this)}
        />
      </div>
    );
  }
}
