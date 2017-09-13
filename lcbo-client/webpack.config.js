
const webpack = require('webpack');
const path = require('path');
const HtmlWebPackPlugin = require('html-webpack-plugin');
const htmlWebPackPluginConfig = new HtmlWebPackPlugin({
	template: 'html/index.html',
	filename: 'index.html',
	inject: 'body'
});

module.exports =  {
	entry: './js/app.js',
	output: {
		path: __dirname,
		filename: 'bundle.js'
	},
	devtool: 'inline-source-map',
	module: {
		loaders: [
			{
				test: /\.css$/,
				loader: 'style-loader!css-loader'
			},
			{
				test: /\.jsx?$/,
				loader: 'babel-loader',
				exclude: /node_modules/,
				query: {
					presets: ['es2015', 'react']
				}
			},
			{
				test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
				loader: 'file-loader?name=fonts/[name].[ext]'
			}
		]
	},
	plugins: [
		htmlWebPackPluginConfig,
		new webpack.ProvidePlugin({ // inject ES5 modules as global vars
			$: 'jquery',
			jQuery: 'jquery',
			'window.jQuery': 'jquery',
			Tether: 'tether'
		})
	]
};
