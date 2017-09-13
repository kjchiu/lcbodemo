import React from 'react';
import PropTypes from 'prop-types';

import {
	Col,
	Image,
	Label,
	Panel
} from 'react-bootstrap';

const Product = ({product}) => {
	var price = '$' + product.priceInCents / 100;
	return (
		<Panel header={product.name} footer={price}>
			<Image src={product.imageThumbUrl} alt={'No image found'} />
		</Panel>
	);
}

Product.propTypes = {
	product: PropTypes.shape({
		name: PropTypes.string.isRequired,
		priceInCents: PropTypes.number.isRequired,
		regularPriceInCents: PropTypes.number.isRequired,
		imageThumbUrl: PropTypes.string
	}).isRequired,
	onSelectPage: PropTypes.func.isRequired
}

export default Product;
