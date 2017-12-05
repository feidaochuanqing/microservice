import React from 'react';
import { Row, Col } from 'antd';
import { Menu, Icon } from 'antd';
import { Tabs, message, Form, Input, Button, Modal, CheckBox, Select, InputNumber } from 'antd';
import debounce from 'lodash.debounce';

const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
const { Option } = Select;

//订单组件
class OrderComponent extends React.Component {
  constructor() {
    super();
    this.state = {
      modalVisible: true,
      editCategoryId: '',
      currentCategory: {},
      categoryMap: [],
      productMap: [],
      selectedCategory: '',
      selectedProduct: ''
    }
  }

  //加载产品分类
  loadCategory() {
    var myFetchOptions = { method: 'GET' };
    const returnjson = {};
    fetch("http://localhost:8011/productCategory/listAll", myFetchOptions)
      .then(response => response.json())
      .then(json => {
        console.log("json->" + json);
        if (json.isOk) {
          const result = json.recordList;
          console.log("result:" + result);
          const data = [];
          result.forEach((r) => {
            data.push({
              value: r.id,
              text: r.categoryName,
            });
            console.log("id:" + r.id + "& category:" + r.categoryName);
          });
          console.log("categories:" + data);
          this.setState({ categoryMap: data });
          console.log("data:" + data);
        }
      });
  }

  //加载产品
  loadProduct() {
    var myFetchOptions = { method: 'GET' };
    const returnjson = {};
    const categoryId = this.state.selectedCategory;
    fetch("http://localhost:8011/product/listAll?categoryId=" + categoryId, myFetchOptions)
      .then(response => response.json())
      .then(json => {
        console.log("json->" + json);
        if (json.isOk) {
          const result = json.recordList;
          console.log("result:" + result);
          const data = [];
          result.forEach((r) => {
            data.push({
              value: r.id,
              text: r.productName,
            });
            console.log("id:" + r.id + "& category:" + r.categoryName);
          });
          console.log("categories:" + data);
          this.setState({ productMap: data });
          console.log("data:" + data);
        }
      });
  }

  //控件加载完成
  componentDidMount() {
    this.setState({ selectedCategory: this.props.categoryId });
    this.setState({ selectedProduct: this.props.productId });
  }

  //设置modal是否可见
  setModalVisible(value) {
    this.setState({ modalVisible: value });
    this.props.afterClosed(value);
  }

  //产品类别选择之后
  afterCategorySelected(value) {
    this.setState({ selectedCategory: value });
  }

  //产品选择之后
  afterProductSelected(value) {
    this.setState({ selectedProduct: value });
  }

  //提交按钮
  handleSubmit(e) {
    e.preventDefault();
    var myFetchOptions = {
      method: 'GET'
    };

    let formData = this.props.form.getFieldsValue();
    let param = "productId=" + this.state.selectedProduct + "&count=" + formData.r_count + "&address=" + formData.r_address;
    let url = formData.r_orderId == '' ? "http://localhost:8011/order/insert?" + param :
      "http://localhost:8011/order/update?" + "orderId=" + formData.r_orderId + "&" + param;
    fetch(url, myFetchOptions)
      .then(response => response.json())
      .then(json => {
        if (json.isOk) {
          message.success(json.message);
        } else {
          message.error(json.message);
        }
      });
    this.setModalVisible(false);
  }

  render() {
    let { getFieldDecorator } = this.props.form;
    let title = this.props.orderId == "" ? "订单添加" : "订单修改";
    let orderId = this.props.orderId;

    let productName = this.props.productName;
    let productId = this.props.productId;

    let categoryName = this.props.categoryName;
    let categoryId = this.props.categoryId;

    let count = this.props.count;
    let address = this.props.address;

    return (
      <Modal title={title}
        wrapClassname="vertical-center-modal"
        visible={true}
        onCancel={() => this.setModalVisible(false)}
        onOk={() => this.setModalVisible(false)}
        okText="关闭"
      >
        <Form horizontal onSubmit={this.handleSubmit.bind(this)}>
          <FormItem label="订单id">
            {getFieldDecorator('r_orderId', { initialValue: orderId || '' })(
              <Input disabled={true} />
            )}
          </FormItem>
          <FormItem label="产品类别">
            {getFieldDecorator('r_productCategoryId', {
              initialValue: categoryName || '',
              rules: [{
                required: true, message: '请选择产品类别!',
              }]
            })(
              <Select showSearch
                style={{ width: 200 }}
                optionFilterProp="children"
                defaultValue={categoryId}
                value={categoryId}
                onSelect={this.afterCategorySelected.bind(this)}
                onFocus={this.loadCategory.bind(this)}
              >
                {this.state.categoryMap.map(d => <Option value={d.value}>{d.text}</Option>)}
              </Select>
              )}
          </FormItem>
          <FormItem label="产品">
            {getFieldDecorator('r_productId', {
              initialValue: productName || '',
              rules: [{
                required: true, message: '请选择产品!',
              }]
            })(
              <Select showSearch
                style={{ width: 200 }}
                optionFilterProp="children"
                defaultValue={productId}
                value={productId}
                onSelect={this.afterProductSelected.bind(this)}
                onFocus={this.loadProduct.bind(this)}
              >
                {this.state.productMap.map(d => <Option value={d.value}>{d.text}</Option>)}
              </Select>
              )}
          </FormItem>
          <FormItem label="数量">
            {getFieldDecorator('r_count', {
              initialValue: count || '',
              rules: [{
                required: true, message: '请输入数量!',
              }]
            })(
              <InputNumber min={1} defaultValue={count} />
              )}
          </FormItem>
          <FormItem label="地址">
            {getFieldDecorator('r_address', {
              initialValue: address || '',
              rules: [{
                required: true, message: '请输入地址!',
              }]
            })(
              <Input disabled={false} />
              )}
          </FormItem>
          <Button type="primary" htmlType="submit" >保存</Button>
        </Form>
      </Modal>
    );
  }
}

export default OrderComponent = Form.create({})(OrderComponent);
