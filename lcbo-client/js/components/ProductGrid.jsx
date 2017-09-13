import React from 'react';
import PropTypes from 'prop-types';

import { connect } from 'react-redux';
import { queryProducts } from '../Actions';

import {
	Col,
	Grid,
	Jumbotron,
	Label,
	Panel,
	Pagination,
	Row
} from 'react-bootstrap';

import Product from './Product.jsx';
import Search from '../containers/Search.jsx';

const COLUMNS = 4;

const ProductGrid = ({items, pageInfo, query, onSelectPage}) => {
	var rows = [];
	let row;
	for (var idx = 0; idx < items.length; ++idx) {
		if (idx % COLUMNS === 0) {
			row = [];
			rows.push(row);
		}
		var item = items[idx];
		row.push((
			<Col xs={6} md={3} key={item.id}>
				<Product product={item} />
			</Col>
		));
	}

	var componentRows = [];
	for (var idxRow = 0, len = rows.length; idxRow < len; ++idxRow) {
		let row = rows[idxRow];
		componentRows.push(<Row key={"row-" + idxRow}>{row}</Row>);
	}

	var numPages = Math.ceil(pageInfo.totalRecordCount / pageInfo.recordsPerPage); 
	return (
		<Grid>
			<Row>
				<Col xs={3}>
					<Search/>
				</Col>
			</Row>
			<Row>
		<Col bsStyle="center-block">
				<Pagination 
					prev
					next
					first
					last
					ellipsis
					boundaryLinks
					items={numPages}
					maxButtons={5}
					activePage={pageInfo.page}
					onSelect={onSelectPage.bind(this, query)} />
		</Col>
			</Row>
			{componentRows}
		</Grid>
	);
};

ProductGrid.propTypes = {
	pageInfo: PropTypes.object,
	items: PropTypes.arrayOf(PropTypes.object)
}

function mapStateToProps(state) {
	var props = state.products
	props.items = props.items || [];
	props.pageInfo = props.pageInfo || {};
	return props;
}

function mapDispatchToProps(dispatch) {
	return {
		onSelectPage: (query, page) => {
			console.log('selecting page', query, page);
			dispatch(queryProducts(query, page));
		}
	}
};


export default connect(
	mapStateToProps,
	mapDispatchToProps
)(ProductGrid);


