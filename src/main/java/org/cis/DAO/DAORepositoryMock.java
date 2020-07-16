package org.cis.DAO;

import org.cis.modello.Repository;

import java.util.ArrayList;
import java.util.List;

public class DAORepositoryMock implements IDAORepository{

    @Override
    public List<Repository> loadRepositories(String nameFile) {
            List<Repository> repositories = new ArrayList<>();

            if (true) {
<<<<<<< HEAD
                repositories.add(new Repository(1, "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169));
                repositories.add(new Repository(4, "2", "bla^3", "", "https://github.com/tomlongo/Flickable.js.git", 321));
                repositories.add(new Repository(4, "456", "bla^3", "", "https://github.com/emergingstack/es-dev-stack.git", 321));
                repositories.add(new Repository(4, "GRTFGR", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 321));
=======
                repositories.add(new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169));
                repositories.add(new Repository("2", "Flickable.js", "bla^3", "", "https://github.com/tomlongo/Flickable.js.git", 3215));
                repositories.add(new Repository("3", "es-dev-stack", "bla^3", "", "https://github.com/emergingstack/es-dev-stack.git", 3201));
                repositories.add(new Repository("4", "ctrlp-cmatcher", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 31));
>>>>>>> MasterLeo
                return repositories;
            }

            if (false) {
                repositories.add(new Repository(1, "pandas", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/pandas-dev/pandas.git", 25169));
                repositories.add(new Repository(4, "spark", "bla^3", "", "https://github.com/apache/spark.git", 321));
                return repositories;
            }

        repositories.add(new Repository(1, "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169));
        repositories.add(new Repository(2, "Github Pages CMS", "bla^3", "https://github.com/jansmolders86/github-pages-cms", "https://github.com/jansmolders86/github-pages-cms.git", 321));
        repositories.add(new Repository(3, "xxx java", "bla bla", "https://github.com/apache/spark", "https://github.com/GillianRickerd/TimedSortingAlgorithms.git", 25132369));
        repositories.add(new Repository(4, "lambdamagicbot", "bla^3", "", "https://github.com/yeahdef/lambdamagicbot.git", 321));
        repositories.add(new Repository(4, "A", "bla^3", "", "https://github.com/Diego81/omnicontacts.git", 321));
        repositories.add(new Repository(4, "B", "bla^3", "", "https://github.com/acatighera/statistics.git", 321));
        repositories.add(new Repository(4, "C", "bla^3", "", "https://github.com/jsdf/coffee-react-transform.git", 321));
        repositories.add(new Repository(4, "D", "bla^3", "", "https://github.com/adamwiggins/scanty.git", 321));
        repositories.add(new Repository(4, "33333333333333333333", "bla^3", "", "https://github.com/rayh/xcoder.git", 321));
        repositories.add(new Repository(4, "E", "bla^3", "", "https://github.com/brianleroux/xui.git", 321));
        repositories.add(new Repository(4, "1", "bla^3", "", "https://github.com/eliaskg/Hacky.git", 321));
        repositories.add(new Repository(4, "2", "bla^3", "", "https://github.com/tomlongo/Flickable.js.git", 321));
        repositories.add(new Repository(4, "3", "bla^3", "", "https://github.com/CEWendel/SECollectionViewFlowLayout.git", 321));
        repositories.add(new Repository(4, "4", "bla^3", "", "https://github.com/jsmestad/pivotal-tracker.git", 321));
        repositories.add(new Repository(4, "5", "bla^3", "", "https://github.com/marcello3d/node-mongolian.git", 321));
        repositories.add(new Repository(4, "6", "bla^3", "", "https://github.com/jlyman/RN-NavigationExperimental-Redux-Example.git", 321));
        repositories.add(new Repository(4, "7", "bla^3", "", "https://github.com/ChrisWren/grunt-nodemon.git", 321));
        repositories.add(new Repository(4, "8", "bla^3", "", "https://github.com/NathanEpstein/D3xter.git", 321));
        repositories.add(new Repository(4, "9", "bla^3", "", "https://github.com/dima/restfulx_framework.git", 321));
        repositories.add(new Repository(4, "11", "bla^3", "", "https://github.com/techiferous/tabulous.git", 321));
        repositories.add(new Repository(4, "12", "bla^3", "", "https://github.com/liveservices/LiveSDK.git", 321));
        repositories.add(new Repository(4, "13", "bla^3", "", "https://github.com/philcn/Auto-Layout-Showcase.git", 321));
        repositories.add(new Repository(4, "14", "bla^3", "", "https://github.com/maccman/acts_as_recommendable.git", 321));
        repositories.add(new Repository(4, "15", "bla^3", "", "https://github.com/bustle/ember-restless.git", 321));
        repositories.add(new Repository(4, "16", "bla^3", "", "https://github.com/callmeed/pop-playground.git", 321));
        repositories.add(new Repository(4, "17", "bla^3", "", "https://github.com/nkohari/jwalk.git", 321));
        repositories.add(new Repository(4, "18", "bla^3", "", "https://github.com/mindsnacks/MSCachedAsyncViewDrawing.git", 321));
        repositories.add(new Repository(4, "FF", "bla^3", "", "https://github.com/RaveJS/rave.git", 321));
        repositories.add(new Repository(4, "GGG", "bla^3", "", "https://github.com/kotojs/kotojs.git", 321));
        repositories.add(new Repository(4, "HTY", "bla^3", "", "https://github.com/colinhowe/monner.git", 321));
        repositories.add(new Repository(4, "RT", "bla^3", "", "https://github.com/crispymtn/linear-partition.git", 321));
        repositories.add(new Repository(4, "VSDFH", "bla^3", "", "https://github.com/rb2k/viddl-rb.git", 321));
        repositories.add(new Repository(4, "DTRYJ", "bla^3", "", "https://github.com/marklarr/mayday.git", 321));
        repositories.add(new Repository(4, "123", "bla^3", "", "https://github.com/EpiphanyMachine/d3AngularIntegration.git", 321));
        repositories.add(new Repository(4, "321", "bla^3", "", "https://github.com/jamesu/railscollab.git", 321));
        repositories.add(new Repository(4, "345", "bla^3", "", "https://github.com/anthonyshort/stitch-css.git", 321));
        repositories.add(new Repository(4, "534", "bla^3", "", "https://github.com/ksuther/KSScreenshotManager.git", 321));
        repositories.add(new Repository(4, "456", "bla^3", "", "https://github.com/emergingstack/es-dev-stack.git", 321));
        repositories.add(new Repository(4, "64", "bla^3", "", "https://github.com/exratione/lambda-complex.git", 321));
        repositories.add(new Repository(4, "654", "bla^3", "", "https://github.com/solderjs/formaline.git", 321));
        repositories.add(new Repository(4, "567", "bla^3", "", "https://github.com/marick/structural-typing.git", 321));
        repositories.add(new Repository(4, "765", "bla^3", "", "https://github.com/MobileMakersEdu/SuProgress.git", 321));
        repositories.add(new Repository(4, "678", "bla^3", "", "https://github.com/fsjack/JKRichTextView.git", 321));
        repositories.add(new Repository(4, "867", "bla^3", "", "https://github.com/DavyJonesLocker/ember-appkit-rails.git", 321));
        repositories.add(new Repository(4, "789", "bla^3", "", "https://github.com/urish/angular-load.git", 321));
        repositories.add(new Repository(4, "978", "bla^3", "", "https://github.com/voloko/twitter-stream.git", 321));
        repositories.add(new Repository(4, "890", "bla^3", "", "https://github.com/npctech/Tattle-UI-iOS.git", 321));
        repositories.add(new Repository(4, "089", "bla^3", "", "https://github.com/Roxasora/RxLabel.git", 321));
        repositories.add(new Repository(4, "CVB", "bla^3", "", "https://github.com/mrwonderman/driveimageview.git", 321));
        repositories.add(new Repository(4, "NBV", "bla^3", "", "https://github.com/basho/riak-ruby-client.git", 321));
        repositories.add(new Repository(4, "GHN", "bla^3", "", "https://github.com/plexus/yaks.git", 321));
        repositories.add(new Repository(4, "88888", "bla^3", "", "https://github.com/desaperados/seed.git", 321));
        repositories.add(new Repository(4, "643563456", "bla^3", "", "https://github.com/jasonkneen/UTiL.git", 321));
        repositories.add(new Repository(4, "WERGTF45", "bla^3", "", "https://github.com/jamis/fuzzyfinder_textmate.git", 321));
        repositories.add(new Repository(4, "N576UN", "bla^3", "", "https://github.com/sapjax/TimoFM.git", 321));
        repositories.add(new Repository(4, "XCQE5GV56JB", "bla^3", "", "https://github.com/elierotenberg/remutable.git", 321));
        repositories.add(new Repository(4, "N78LM", "bla^3", "", "https://github.com/jeresig/fireunit.git", 321));
        repositories.add(new Repository(4, "M689NBV", "bla^3", "", "https://github.com/drnic/appscrolls.git", 321));
        repositories.add(new Repository(4, "B56U", "bla^3", "", "https://github.com/s-a/sublime-text-refactor.git", 321));
        repositories.add(new Repository(4, "748569", "bla^3", "", "https://github.com/facebookarchive/immutable-re.git", 321));
        repositories.add(new Repository(4, "12369", "bla^3", "", "https://github.com/Seitk/InfiniteScrollPicker.git", 321));
        repositories.add(new Repository(4, "ASDE34", "bla^3", "", "https://github.com/gigafied/ember-animate.git", 321));
        repositories.add(new Repository(4, "JU7890P", "bla^3", "", "https://github.com/drnic/copy-as-rtf-tmbundle.git", 321));
        repositories.add(new Repository(4, "11111111111111", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 321));
        repositories.add(new Repository(4, "444444444444444444", "bla^3", "", "https://github.com/blahed/chainsaw.git", 321));
        repositories.add(new Repository(4, "66666666666666666666666666", "bla^3", "", "https://github.com/xincc/CCRequest.git", 321));
        repositories.add(new Repository(4, "TTTTTTTTTTTTTT", "bla^3", "", "https://github.com/tmm1/hotspots.git", 321));
        repositories.add(new Repository(4, "YYYYYYYYYYYYYYYYYYYYY", "bla^3", "", "https://github.com/jack7890/mkdown.git", 321));

        repositories.add(new Repository(4, "AQWSEDRFTGYH", "bla^3", "", "https://github.com/jeresig/fireunit.git", 321));
        repositories.add(new Repository(4, "97531", "bla^3", "", "https://github.com/drnic/appscrolls.git", 321));
        repositories.add(new Repository(4, "246810", "bla^3", "", "https://github.com/s-a/sublime-text-refactor.git", 321));
        repositories.add(new Repository(4, "ASWQ55555555555555", "bla^3", "", "https://github.com/facebookarchive/immutable-re.git", 321));
        repositories.add(new Repository(4, "RRRRRRRRRRRRRR", "bla^3", "", "https://github.com/Seitk/InfiniteScrollPicker.git", 321));
        repositories.add(new Repository(4, "55555555555555555555555", "bla^3", "", "https://github.com/gigafied/ember-animate.git", 321));
        repositories.add(new Repository(4, "YYYHHUJYJUH", "bla^3", "", "https://github.com/drnic/copy-as-rtf-tmbundle.git", 321));
        repositories.add(new Repository(4, "GRTFGR", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 321));
        repositories.add(new Repository(4, "BNMJHG", "bla^3", "", "https://github.com/blahed/chainsaw.git", 321));
        repositories.add(new Repository(4, "GNJMHB", "bla^3", "", "https://github.com/xincc/CCRequest.git", 321));
        repositories.add(new Repository(4, "09OP0DCXZ2Q", "bla^3", "", "https://github.com/tmm1/hotspots.git", 321));
        repositories.add(new Repository(4, "123652712884045", "bla^3", "", "https://github.com/jack7890/mkdown.git", 321));

        repositories.add(new Repository(4, "asderfgtfrg", "bla^3", "", "https://github.com/jeresig/fireunit.git", 321));
        repositories.add(new Repository(4, "3454yhnd47", "bla^3", "", "https://github.com/drnic/appscrolls.git", 321));
        repositories.add(new Repository(4, "asas323232323", "bla^3", "", "https://github.com/s-a/sublime-text-refactor.git", 321));
        repositories.add(new Repository(4, "890olkililok", "bla^3", "", "https://github.com/facebookarchive/immutable-re.git", 321));
        repositories.add(new Repository(4, "kkkxzwa", "bla^3", "", "https://github.com/Seitk/InfiniteScrollPicker.git", 321));
        repositories.add(new Repository(4, "27172854", "bla^3", "", "https://github.com/gigafied/ember-animate.git", 321));
        repositories.add(new Repository(4, "903x", "bla^3", "", "https://github.com/drnic/copy-as-rtf-tmbundle.git", 321));
        repositories.add(new Repository(4, "zxcvvcxz1", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 321));
        repositories.add(new Repository(4, "7458963652", "bla^3", "", "https://github.com/blahed/chainsaw.git", 321));
        repositories.add(new Repository(4, "xxx", "bla^3", "", "https://github.com/xincc/CCRequest.git", 321));
        repositories.add(new Repository(4, "yyy", "bla^3", "", "https://github.com/tmm1/hotspots.git", 321));
        repositories.add(new Repository(4, "zzz", "bla^3", "", "https://github.com/jack7890/mkdown.git", 321));

        repositories.add(new Repository(4, "acsd", "bla^3", "", "https://github.com/techiferous/tabulous.git", 321));
        repositories.add(new Repository(4, "1a2s", "bla^3", "", "https://github.com/liveservices/LiveSDK.git", 321));
        repositories.add(new Repository(4, "2q2w3e", "bla^3", "", "https://github.com/philcn/Auto-Layout-Showcase.git", 321));
        repositories.add(new Repository(4, "l30", "bla^3", "", "https://github.com/maccman/acts_as_recommendable.git", 321));
        repositories.add(new Repository(4, "jjam", "bla^3", "", "https://github.com/bustle/ember-restless.git", 321));
        repositories.add(new Repository(4, "16w9", "bla^3", "", "https://github.com/zythum/weibotuchuang.git", 321));
        repositories.add(new Repository(4, "1709ihVS", "bla^3", "", "https://github.com/nkohari/jwalk.git", 321));
        repositories.add(new Repository(4, "18asda3e", "bla^3", "", "https://github.com/mindsnacks/MSCachedAsyncViewDrawing.git", 321));
        repositories.add(new Repository(4, "FFaq213e4r", "bla^3", "", "https://github.com/jnunemaker/joint.git", 321));
        repositories.add(new Repository(4, "GGG0o9iu7", "bla^3", "", "https://github.com/kotojs/kotojs.git", 321));
        repositories.add(new Repository(4, "HTY765r4e321", "bla^3", "", "https://github.com/colinhowe/monner.git", 321));
        repositories.add(new Repository(4, "RTh765reAS", "bla^3", "", "https://github.com/substack/covert.git", 321));
        repositories.add(new Repository(4, "VSDFH473axcccccc", "bla^3", "", "https://github.com/rb2k/viddl-rb.git", 321));
        repositories.add(new Repository(4, "DTRYJ12365o8SCD", "bla^3", "", "https://github.com/SlyMarbo/spdy.git", 321));

            return repositories;
    }

    @Override
    public void saveRepositories(String directoryFiles, List<Repository> repositories) {}
}
