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
				<Grid>
					<Row>
						<User />
					</Row>
					<Row>
						<Col xs={4}>
							<Search/>
						</Col>
					</Row>
					<Row>
						<ProductGrid />
					</Row>
				</Grid>
			</div>
		);
	}
}
