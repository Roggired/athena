import { Configuration } from 'webpack'
import { module } from '../shared/module'
import { build, src } from '../shared/paths'
import { createFileName } from '../shared/utils'
import { plugins } from './plugins'
import { resolve } from './resolve'

export const devConfiguration: Configuration = {
    name: 'dev-configuration',
    context: src,
    mode: 'development',
    entry: './index.tsx',
    output: {
        path: build,
        filename: createFileName('static/js/'),
        // TODO to prod use hash
        assetModuleFilename: 'assets/[name][ext]',
        clean: true,
        publicPath: '/virtual-lab/',
    },
    devtool: 'source-map',
    resolve,
    module,
    plugins,
}
