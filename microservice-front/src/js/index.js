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
    return <div className="wrapper" id="example">
    <header className="main-header">
      <a href="#" className="logo">
        <span className="logo-mini"><b>A</b>LT</span>
        <span className="logo-lg"><b>微服务框架</b></span>
      </a>
      <nav className="navbar navbar-static-top">
        <a href="#" className="sidebar-toggle" data-toggle="push-menu" role="button">
          <span className="sr-only">Toggle navigation</span>
        </a>
  
        <div className="navbar-custom-menu">
          <ul className="nav navbar-nav">
            <li className="dropdown user user-menu">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <img src="src/images/user2-160x160.jpg" className="user-image" alt="User Image" />
                <span className="hidden-xs">王小明</span>
              </a>
            </li>
          </ul>
        </div>
      </nav>
    </header>
  
    <aside className="main-sidebar">
      <section className="sidebar">
        <div className="user-panel">
          <div className="pull-left image">
            <img src="src/images/user2-160x160.jpg" className="img-circle" alt="User Image" />
          </div>
          <div className="pull-left info">
            <p>王小明</p>
            <a href="#"><i className="fa fa-circle text-success"></i> 在线</a>
          </div>
        </div>
        
        <ul className="sidebar-menu" data-widget="tree">
          <li className="header">导航</li>
           
          <li className="treeview">
            <a href="#">
              <i className="fa fa-table"></i> <span>产品管理</span>
              <span className="pull-right-container">
                <i className="fa fa-angle-left pull-right"></i>
              </span>
            </a>
            <ul className="treeview-menu">
              <li><a component="ProductListComponent" visitorPath="系统首页/主数据/产品列表" onClick={this.nav.bind(this)}><i className="fa fa-circle-o"></i> 产品列表</a></li>
              <li><a component="ProductCategoryListComponent" visitorPath="系统首页/主数据/产品类别列表" onClick={this.nav.bind(this)}><i className="fa fa-circle-o"></i>产品类别列表</a></li>
            </ul>
          </li>
      
      <li className="treeview">
            <a href="#">
              <i className="fa fa-table"></i> <span>交易管理</span>
              <span className="pull-right-container">
                <i className="fa fa-angle-left pull-right"></i>
              </span>
            </a>
            <ul className="treeview-menu">
              <li><a component="OrderListComponent" visitorPath="系统首页/交易管理/订单列表" onClick={this.nav.bind(this)}><i className="fa fa-circle-o"></i> 订单管理</a></li>
            </ul>
          </li>
        </ul>
      </section>
    </aside>
    <div className="content-wrapper" style={{ padding: '0 24px 24px' }}>
      <section className="content">
        <Breadcrumb style={{ margin: '12px 0' }}>
              {path.map(function (item) {
                return <Breadcrumb.Item>{item}</Breadcrumb.Item>;
              })
              }
            </Breadcrumb>
        
        {currentComponent}
        
    </section>
    </div>
  
    <footer className="main-footer">
      <div className="pull-right hidden-xs">
        <b>Version</b> 1.0.0
      </div>
      <strong>Copyright © 2017</strong> All rights
      reserved.
    </footer>
  </div>;
  }
}

ReactDOM.render(<IndexComponent />, document.getElementById('example'));
