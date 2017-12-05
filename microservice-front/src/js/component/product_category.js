import React from 'react';
import { Row, Col } from 'antd';
import { Menu, Icon } from 'antd';
import { Tabs, message, Form, Input, Button, Modal, CheckBox, Select } from 'antd';
import debounce from 'lodash.debounce';

const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
const { Option } = Select;


//产品类别组件
class ProductCategoryComponet extends React.Component {
  constructor() {
    super();
    this.state = {
      current: "top",
      modalVisible: true,
      action: 'login',
      hasLogined: false,
      userNickName: '',
      userid: 0,
      editCategoryId: '',
      currentCategory: {},
      map: []
    }
  }

  //设置modal是否可见
  setModalVisible(value) {
    this.setState({ modalVisible: value });
    this.props.afterClosed(value);
  }

  //处理提交事件
  handleSubmit(e) {
    e.preventDefault();
    var myFetchOptions = {
      method: 'GET'
    };

    var formData = this.props.form.getFieldsValue();

    let param = "categoryCode=" + formData.r_categoryCode + "&categoryName=" + formData.r_categoryName;
    let url = formData.r_categoryId == '' ? "http://localhost:8011/productCategory/insert?" + param :
      "http://localhost:8011/productCategory/update?" + "categoryId=" + formData.r_categoryId + "&" + param;
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
    let title = this.props.categoryId == "" ? "产品类别添加" : "产品类别修改";
    let categoryId = this.props.categoryId;
    let categoryCode = this.props.categoryCode;
    let categoryName = this.props.categoryName;

    return (
      <Modal title={title}
        wrapClassname="vertical-center-modal"
        visible={true}
        onCancel={() => this.setModalVisible(false)}
        onOk={() => this.setModalVisible(false)}
        okText="关闭"
      >
        <Form horizontal onSubmit={this.handleSubmit.bind(this)}>
          <FormItem label="产品类别id">
            {getFieldDecorator('r_categoryId', {
              initialValue: categoryId || ''
            })(
              <Input disabled={true} />
              )}
          </FormItem>
          <FormItem label="产品类别编码">
            {getFieldDecorator('r_categoryCode', {
              initialValue: categoryCode || '',
              rules: [{
                required: true, message: '请输入产品类别编码!',
              }]

            })(
              <Input />
              )}
          </FormItem>
          <FormItem label="产品类别名称">
            {getFieldDecorator('r_categoryName', {
              initialValue: categoryName || '',
              rules: [{
                required: true, message: '请输入类别名称!',
              }]
            })(
              <Input />
              )}
          </FormItem>
          <Button type="primary" htmlType="submit" >保存</Button>
        </Form>
      </Modal>
    );
  }
}

export default ProductCategoryComponet = Form.create({})(ProductCategoryComponet);
