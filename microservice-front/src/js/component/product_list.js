import React from 'react';
import ReactDOM from 'react-dom';
import { Table, Button, Icon, Popconfirm, message } from 'antd';
import { Input } from 'antd';
import reqwest from 'reqwest';
import ProductComponent from './product.js'
import PubSub from 'pubsub-js';

const Search = Input.Search;
const deleteProductEvent = "deleteProduct";
const updateProductEvent = "updateProduct";

//产品列表组件
export default class ProductListComponent extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      pagination: {},
      loading: false,
      productName: '',
      editProduct: {},
      isShowProduct: false
    }
  }

  //删除之后进行提示并重新加载数据
  onDelete(e, key) {
    var myFetchOptions = { method: 'GET' };
    const returnjson = {};
    let url = "http://localhost:8011/product/delete?productId=" + key;
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
    this.setState({ editProduct: product, isShowProduct: true });
  }

  //分页查询
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
  handleShowProduct(e) {
    let editProduct = { id: '', productCode: '', productName: '', productCategory: { id: '', categoryName: '' } };
    this.setState({ editProduct: editProduct, isShowProduct: true });
  }

  fetch(params = {}) {
    console.log('params:', params);
    this.setState({ loading: true });
    reqwest({
      url: 'http://localhost:8011/product/listProductsByPage',
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
    PubSub.unsubscribe(deleteProductEvent);
    PubSub.unsubscribe(updateProductEvent);
    PubSub.subscribe(deleteProductEvent, this.onDelete.bind(this));
    PubSub.subscribe(updateProductEvent, this.onUpdate.bind(this));
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
      productName: value
    };
    this.setState({
      productName: value
    });
    this.fetch(params);
  }

  render() {
    let columns = [{
      title: '产品编号',
      dataIndex: 'productCode',
      width: '20%',
      key: 'productCode'
    }, {
      title: '名称',
      dataIndex: 'productName',
      width: '20%',
      key: 'productName'
    }, {
      title: '类别',
      dataIndex: 'productCategory.categoryName',
      key: 'id'
    }, {
      title: '操作',
      key: 'id',
      render(text, record) {
        return (
          <span>
            <Popconfirm title="确定删除么？?"
              onConfirm={() => {
                PubSub.publish(deleteProductEvent, record.id);
              }}>
              <a href="#">删除</a>
            </Popconfirm>
            &nbsp;&nbsp;&nbsp;&nbsp;
              <a href="#" onClick={() => {
              PubSub.publish(updateProductEvent, record);
            }} >修改</a>
          </span>
        );
      }
    }];
    console.log("this.state.editproductid->" + this.state.editProduct.id);

    let showProductComp = this.state.isShowProduct ? <ProductComponent
      productId={this.state.editProduct.id}
      productCode={this.state.editProduct.productCode}
      productName={this.state.editProduct.productName}
      category={this.state.editProduct.productCategory.categoryName}
      categoryId={this.state.editProduct.productCategory.id}
      afterClosed={this.afterClosed.bind(this)} /> : null;
    return (
      <div>
        {showProductComp}
        <div style={{ marginBottom: 16 }}>
          <Search
            placeholder="产品名称"
            style={{ width: 200 }}
            onSearch={this.serchClick.bind(this)}
          />
          <Button style={{ float: `right` }}
            size="large" type="ghost" onClick={this.handleShowProduct.bind(this)}
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
