import React from 'react';
import ReactDOM from 'react-dom';
import { Table, Button, Icon, Popconfirm, message } from 'antd';
import { Input } from 'antd';
import reqwest from 'reqwest';
import OrderComponent from './order.js'
import PubSub from 'pubsub-js';

const Search = Input.Search;
const deleteOrderEvent = "deleteOrder";
const updateProductyEvent = "updateProduct";

//订单列表组件
export default class OrderComponentList extends React.Component {
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
      categoryName: '',
      editOrder: {},
      isShowOrder: false,
      queryProductName: 'all'
    }
  }

  //删除之后进行提示并重新加载数据
  onDelete(e, key) {
    var myFetchOptions = { method: 'DELETE' };
    const returnjson = {};
    let url = "http://localhost:8011/order/" + key;
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
  onUpdate(e, order) {
    this.setState({ editOrder: order, isShowOrder: true });
  }

  //分页查询
  handleTableChange(pagination, filters, sorter) {
    this.state.pagination.current = pagination.current;
    this.loadData();
  }

  //创建框
  handleShowCategory(e) {
    let editOrder = {
      id: '',
      count: '',
      address: '',
      product: {
        id: "",
        productName: "",
        productCategory: {
          id: "",
          categoryName: ""
        }
      }
    };
    this.setState({ editOrder: editOrder, isShowOrder: true });
  }


  loadData(params = {}) {
    console.log('params:', params);
    this.setState({ loading: true });

    params.pageNum = this.state.pagination.current;
    params.numPerPage = this.state.pagination.pageSize;

    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    let url = 'http://localhost:8011/order/listByPage/' + this.state.queryProductName;
    let request = new Request(url, {
      method: 'POST',
      mode: 'cors',
      body: JSON.stringify(params),
      headers: myHeaders
    });

    fetch(request)
      .then(response => response.json())
      .then(data => {
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
    PubSub.unsubscribe(deleteOrderEvent);
    PubSub.unsubscribe(updateProductyEvent);
    PubSub.subscribe(deleteOrderEvent, this.onDelete.bind(this));
    PubSub.subscribe(updateProductyEvent, this.onUpdate.bind(this));
    this.loadData();
  }

  //产品窗口关闭之后的事件处理
  afterClosed(value) {
    this.setState({
      isShowOrder: false
    });
    this.loadData();
  }

  //查询按钮
  serchClick(value) {
    console.log("pagination:" + this.state.pagination);
    this.state.pagination.current = 1;
    this.state.queryProductName = (value == "" ? "all" : value);
    this.loadData();
  }

  render() {
    let columns = [{
      title: '订单编号',
      dataIndex: 'id',
      width: '25%',
      key: 'id'
    }, {
      title: '数量',
      dataIndex: 'count',
      width: '5%',
      key: 'count'
    }, {
      title: '产品类别',
      dataIndex: '',
      width: '10%',
      key: 'productId',
      render: (text, record, index) => record.product.productCategory.categoryName
    }, {
      title: '产品',
      dataIndex: '',
      width: '15%',
      key: 'productId',
      render: (text, record, index) => record.product.productName
    }, {
      title: '地址',
      dataIndex: 'address',
      width: '35%',
      key: 'address'
    }, {
      title: '操作',
      key: 'operate',
      render(text, record) {
        return (
          <span>
            <Popconfirm title="确定删除么？?"
              onConfirm={() => {
                PubSub.publish(deleteOrderEvent, record.id);
              }}>
              <a href="#">删除</a>
            </Popconfirm>
            &nbsp;&nbsp;&nbsp;&nbsp;
              <a href="#" onClick={() => {
              PubSub.publish(updateProductyEvent, record);
            }} >修改</a>
          </span>
        );
      }
    }];


    let showOrderComp = this.state.isShowOrder ? <OrderComponent
      orderId={this.state.editOrder.id}

      categoryId={this.state.editOrder.product.productCategory.id}
      categoryName={this.state.editOrder.product.productCategory.categoryName}

      productId={this.state.editOrder.product.id}
      productName={this.state.editOrder.product.productName}

      count={this.state.editOrder.count}
      address={this.state.editOrder.address}

      afterClosed={this.afterClosed.bind(this)}
    /> : null;
    return (
      <div>
        {showOrderComp}
        <div style={{ marginBottom: 16 }}>
          <Search
            placeholder="产品名称"
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
