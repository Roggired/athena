import * as path from 'path'
import { WebpackPluginInstance } from 'webpack'
import { build, publicPath } from '../shared/paths'
import MiniCssExtractPlugin = require('mini-css-extract-plugin')

const Dotenv = require('dotenv-webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')

export const plugins: WebpackPluginInstance[] = [
    new Dotenv(),
    new HtmlWebpackPlugin({
        title: 'Virtual lab',
        template: path.resolve(publicPath, 'index.html'),
    }),
    new CopyWebpackPlugin({
        patterns: [
            {
                from: path.resolve(publicPath, 'favicon.ico'),
                to: build,
            },
        ],
    }),
    new MiniCssExtractPlugin({
        filename: 'static/css/[name].css',
    }),
]
