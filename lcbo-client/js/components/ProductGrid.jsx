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

const COLUMNS = 4;

const ProductGrid = ({items, pageInfo, query, onSelectPage}) => {
	if (! items || items.length === 0) {
		return (<Jumbotron><h1>No matches found</h1></Jumbotron>);
	}

	var rows = [];
	let row;
	for(var idx = 0; idx < items.length; ++idx) {
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
	for(var idxRow =0, len = rows.length; idxRow < len; ++idxRow) {
		let row = rows[idxRow];
		componentRows.push(<Row key={"row-" + idxRow}>{row}</Row>);
	}

	var numPages = Math.ceil(pageInfo.totalRecordCount / pageInfo.recordsPerPage); 
	return (
		<Grid>
			<Row>
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
	console.log('product grid props', props);
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


