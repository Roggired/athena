import { ModuleOptions } from 'webpack'

export const module: ModuleOptions = {
    rules: [
        {
            test: /\.(tsx|ts)$/,
            exclude: /node_modules/,
            use: 'ts-loader',
        },
        {
            test: /\.(png|svg|jpg|gif)$/,
            type: 'asset',
        },
        {
            test: /\.s[ac]ss$/i,
            use: ['style-loader', 'css-loader', 'sass-loader'],
        },
    ],
}
