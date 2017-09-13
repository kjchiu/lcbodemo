import React from 'react';
import {
	Col,
	FormControl,
	Grid,
	Nav,
	Navbar,
	NavItem,
	Panel,
	Row
} from 'react-bootstrap';

import Search from '../containers/Search.jsx';
import User from '../containers/User.jsx';
import ProductGrid from '../components/ProductGrid.jsx';

export default class App extends React.Component {

	render() {
		return (
			<div>
				<Navbar>
					<Navbar.Header>
						<Navbar.Brand>
							<a href="#">LCBO Wrapper</a>
						</Navbar.Brand>
					</Navbar.Header>
					<Navbar.Collapse>
						<User />
					</Navbar.Collapse>
				</Navbar>
				<Grid>
					<Row>
					</Row>
					<Row bsStyle="text-center">
						<ProductGrid />
					</Row>
				</Grid>
			</div>
		);
	}
}
