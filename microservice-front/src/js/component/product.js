import React from 'react';
import {Row, Col} from 'antd';
import { Menu, Icon } from 'antd';
import { Tabs,  message,  Form,  Input, Button,  Modal, CheckBox, Select } from 'antd';
import debounce from 'lodash.debounce';

const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
const { Option } = Select;


class ProductComponet extends React.Component {
  constructor() {
    super();
    this.state = {
      current:"top",
      modalVisible :true,
      action : 'login',
      hasLogined : false,
      userNickName: '',
      userid:0,
      editProductId:'',
      currentProduct:{},
      map:[]
    }
    //this.loadCategory = debounce(this.loadCategory, 800);
  }

  setModalVisible(value) {
    this.setState({modalVisible:value});
    this.props.afterClosed(value);
  }

  loadCategory() {
    var myFetchOptions = {method:'GET'};
    const returnjson = {};
    fetch("http://localhost:8011/productCategory/listAll",myFetchOptions)
        .then(response=>response.json())
        .then(json=>{
          console.log("json->" + json);
          if(json.isOk) {
            const result = json.recordList;
            console.log("result:" + result);
            const data = [];
            result.forEach((r) => {
                    data.push({
                      value: r.id,
                      text: r.categoryName,
                    });
                    console.log("id:" + r.id +"& category:" + r.categoryName);
                  });
            console.log("categories:" + data);
            this.setState({map:data});
            console.log("data:" + data);
          }
        });
  }


  handleSubmit(e) {
    e.preventDefault();
    var myFetchOptions = {
      method : 'GET'
    };

    var formData =  this.props.form.getFieldsValue();
    console.log("form data is ->" + formData);
    console.log("form data productcode is ->" + formData.r_productCode);
    let param = "productCode=" + formData.r_productCode +"&productName=" + formData.r_productName + "&productCategory=" + formData.r_productCategory;
    let url = formData.r_productId == '' ? "http://localhost:8011/product/insert?"+param:
      "http://localhost:8011/product/update?"+"productId=" + formData.r_productId +"&" + param;
    fetch(url, myFetchOptions)
      .then(response=>response.json())
      .then(json=>{
        if(json.isOk) {
            message.success(json.message);
        } else {
            message.error(json.message);
        }
    });
    this.setModalVisible(false);
  }

  render() {
    let { getFieldDecorator } = this.props.form;
    let title = this.props.productId == ""?"产品添加":"产品修改";
    let productId = this.props.productId;
    let productCode = this.props.productCode;
    let productName = this.props.productName;
    let category = this.props.category;
    let categoryId = this.props.categoryId;

    console.log("product component : productId:" + productId);
    console.log("product component : productCode:" + productCode);
    console.log("product component : productName:" + productName);
    console.log("product component : category:" + category);

    return (
    <Modal title={title}
      wrapClassname="vertical-center-modal"
      visible = {true}
      onCancel={()=>this.setModalVisible(false)}
      onOk={()=>this.setModalVisible(false)}
      okText="关闭"
    >
     <Form horizontal onSubmit={this.handleSubmit.bind(this)}>
     <FormItem   label="产品id">
       {getFieldDecorator('r_productId', {initialValue:productId || ''})(
         <Input disabled={true} />
       )}
     </FormItem>
     <FormItem label="产品分类">
       {getFieldDecorator('r_productCategory',
       {
         initialValue:category || ''  ,
         rules: [{
             required: true, message: '请选择产品分类',
           }]

       })(
          <Select showSearch
             style={{ width: 200 }}
             optionFilterProp="children"
             defaultValue={categoryId}
             value={categoryId}
             onFocus={this.loadCategory.bind(this)}
           >
           {this.state.map.map(d => <Option value={d.value}>{d.text}</Option>)}
           </Select>
       )}
     </FormItem>
       <FormItem   label="产品编码">
         {getFieldDecorator('r_productCode',
         {
           initialValue:productCode || '' ,
           rules: [{
             required: true, message: '请输入产品编码!',
           }]
       })(
           <Input />
         )}
       </FormItem>
       <FormItem label="产品名称">
         {getFieldDecorator('r_productName',
         {
           initialValue:productName || '' ,
           rules: [{
               required: true, message: '请输入产品名称!',
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

 export default ProductComponet = Form.create({})(ProductComponet);
