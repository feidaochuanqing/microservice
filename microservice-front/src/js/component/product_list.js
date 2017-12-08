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
      pagination: {
        total : 10,
        pageSize : 10,
        current : 1
      },
      loading: false,
      productName: 'all',
      editProduct: {},
      isShowProduct: false
    }
  }

  //删除之后进行提示并重新加载数据
  onDelete(e, key) {
    var myFetchOptions = { method: 'DELETE' };
    const returnjson = {};
    let url = "http://localhost:8011/product/" + key;
    fetch(url, myFetchOptions)
      .then(response => response.json())
      .then(json => {
        console.log("json->" + json);
        if (json.isOk) {
          message.success(json.message);
          this.loadData();
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
    this.state.pagination.current = pagination.current;
    this.loadData();
  }

  //创建框
  handleShowProduct(e) {
    let editProduct = { id: '', productCode: '', productName: '', productCategory: { id: '', categoryName: '' } };
    this.setState({ editProduct: editProduct, isShowProduct: true });
  }

  loadData(params = {}) {
    this.setState({ loading: true });

    params.pageNum = this.state.pagination.current;
    params.numPerPage = this.state.pagination.pageSize;

    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    let url = 'http://localhost:8011/product/listByPage/' + this.state.productName;
    let request = new Request(url, {
          method: 'POST', 
          mode: 'cors',
          body:JSON.stringify(params),
          headers:myHeaders
    });

    fetch(request)
      .then(response=>response.json())
      .then(data=>{
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
    this.loadData();
  }

  //产品窗口关闭之后的事件处理
  afterClosed(value) {
    this.setState({
      isShowProduct: false
    });
    this.loadData();
  }

  //搜索
  serchClick(value) {
    console.log("pagination:" + this.state.pagination);
    this.state.productName = (value == "" ? "all" : value);
    this.state.pagination.current = 1;
    this.loadData();
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
