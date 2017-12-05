import 'antd/dist/antd.css';

var React = require('react');
var ReactDOM = require('react-dom');

import { Layout, Menu, Breadcrumb, Icon, Button } from 'antd';
const { SubMenu } = Menu;
const { Header, Content, Sider } = Layout;

import ProductListComponent from './component/product_list.js';
import ProductCategoryListComponent from './component/product_category_list.js';
import OrderListComponent from './component/order_list.js';


export default class IndexComponent extends React.Component {

  nav(e) {
    e.preventDefault();
    let componetName = e.target.getAttribute('component');
    let visitorPath = e.target.getAttribute('visitorPath');
    let visitorPaths = visitorPath.split('/');
    this.setState({ currentComponent: componetName, path: visitorPaths });

  }

  constructor() {
    super();
    this.state = { currentComponent: "", path: [] };
  }


  render() {
    let currentComponent = null;

    //alert(this.state.currentComponent);
    if (this.state.currentComponent == "ProductListComponent") {
      currentComponent = <ProductListComponent />
    } else if (this.state.currentComponent == "OrderListComponent") {
      currentComponent = <OrderListComponent />
    } else if (this.state.currentComponent == "ProductCategoryListComponent") {
      currentComponent = <ProductCategoryListComponent />
    } else {
      currentComponent = <ProductListComponent />
    }

    let path = ["系统首页", "主数据", "产品列表"];
    if (this.state.path.length > 0) {
      path = this.state.path;
    }
    return <div>

      <Layout>
        <Header className="header" style={{ background: 'white' }}>
          <div >
            <div className="logo">微服务架构</div>
          </div>
        </Header>
        <Layout>
          <Sider width={200} style={{ background: '#fff' }}>
            <Menu
              mode="inline"
              defaultSelectedKeys={['1']}
              defaultOpenKeys={['sub1', 'sub2']}
              style={{ height: '100%' }}
            >
              <SubMenu key="sub1" title={<span><Icon type="user" />产品管理</span>}>
                <Menu.Item key="1">
                  <a component="ProductListComponent" visitorPath="系统首页/主数据/产品列表" onClick={this.nav.bind(this)}>产品列表</a>
                </Menu.Item>
                <Menu.Item key="2">
                  <a component="ProductCategoryListComponent" visitorPath="系统首页/主数据/产品类别列表" onClick={this.nav.bind(this)}>产品类别列表</a>
                </Menu.Item>
              </SubMenu>
              <SubMenu key="sub2" title={<span><Icon type="laptop" />交易管理</span>}>
                <Menu.Item key="5">
                  <a component="OrderListComponent" visitorPath="系统首页/交易管理/订单列表" onClick={this.nav.bind(this)}>订单列表</a>
                </Menu.Item>
              </SubMenu>
            </Menu>
          </Sider>
          <Layout style={{ padding: '0 24px 24px' }}>
            <Breadcrumb style={{ margin: '12px 0' }}>
              {path.map(function (item) {
                return <Breadcrumb.Item>{item}</Breadcrumb.Item>;
              })
              }
            </Breadcrumb>
            <Content style={{ background: '#fff', padding: 24, margin: 0, minHeight: 580 }}>
              {currentComponent}
            </Content>
          </Layout>
        </Layout>
      </Layout>

    </div>;
  }
}

ReactDOM.render(<IndexComponent />, document.getElementById('example'));
