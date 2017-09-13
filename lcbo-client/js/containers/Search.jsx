import {
	Form,
	FormControl,
	FormGroup,
	Modal
} from 'react-bootstrap';

import React from 'react';
import { connect } from 'react-redux';

import { queryProducts } from '../Actions';

class Search extends React.Component {

	render() {
		return (
			<div>
				<Form horizontal onSubmit={this.props.onSearch}>
				<FormControl placeholder={"Search..."} />
				</Form >
			</div>
		);
	}
}

function mapStateToProps(state) {
	return {};
}

function mapDispatchToProps(dispatch) {
	return {
		onSearch: (e) => {
			e.preventDefault();
			// barf
			var query = e.target.elements[0].value;
			dispatch(queryProducts(query, 1));
			return false;
		}
	}
};


const SearchContainer = connect(
	mapStateToProps,
	mapDispatchToProps
)(Search);

export default SearchContainer;
